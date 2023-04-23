package pl.fryciarnia.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.session.DbSession;
import pl.fryciarnia.session.SessionController;
import pl.fryciarnia.utils.APIDatagram;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static pl.fryciarnia.session.SessionController.newFromUserUUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class UserMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @PostMapping("/api/user/ping")
    @ResponseBody
    public String APIDbUserPing (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();
        DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
        if (dbUser == null)
            return apiDatagram.fail("użytkownik nie istnieje");

        dbUser.setPassword("");
        dbUser.setMail("");
        apiDatagram.setData(dbUser);

        return apiDatagram.success();
    }

    @PostMapping("/api/user/register")
    @ResponseBody
    public String APIDbUserRegister (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
//        System.out.println("RED COOKIE " + testCookieRed);
        APIDatagram apiDatagram = new APIDatagram();
        DbUser wu = DbUser.fromJSON(body);

        /* create new uuid */
        wu.setUuid(String.valueOf(UUID.randomUUID()));

        /* validate rest of fields */
        apiDatagram.setOk(true);

        if (wu.getMail() == null)
        {
            apiDatagram.setOk(false);
            apiDatagram.setMsg("Błędny format maila");
        }

        if (wu.getPassword() == null)
        {
            apiDatagram.setOk(false);
            apiDatagram.setMsg("Błędne hasło");
        }

        if (wu.getType() == null)
        {
            apiDatagram.setOk(false);
            apiDatagram.setMsg("Błędny typ konta");
        }

        /* if all checks succeed, create new instance of DbUser in DB */
        if (apiDatagram.isOk())
        {
            if (!UserController.insertUser(jdbcTemplate, wu))
            { /* insert failed */
                apiDatagram.setOk(false);
                apiDatagram.setMsg("Połączenie z bazą danych się nie powiodło");
            }
        }

        /* if all checked, create new session for this user UNLESS a session allready exists  */
        DbUser currentSessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
        if (currentSessionUser == null || !currentSessionUser.getType().equals(UserType.Admin))
        {
            Timestamp expiration = new Timestamp(System.currentTimeMillis() + 1000L * 3600);
            DbSession dbSession = newFromUserUUID(jdbcTemplate, wu.getUuid(), expiration);
            ResponseCookie responseCookie = ResponseCookie.from("fry_sess", dbSession.getToken())
                .httpOnly(true)
                .sameSite("Strict")
                .secure(false)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();
            httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }

        apiDatagram.setData(wu);
        return apiDatagram.toString();
    }

    @SneakyThrows
    @PostMapping("/api/user/info")
    @ResponseBody
    public String APIDbUserInfo (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();

        /* either by json or session */
        String userToCheckUuid = null;
        Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);

        if (m.containsKey("uuid"))
            userToCheckUuid = (String) m.get("uuid");

        /* find user by session id unless its nil */
        if (userToCheckUuid == null)
        {
            if (frySess.equals("nil"))
                return apiDatagram.fail("No session");

            DbSession dbSession = SessionController.getSessionByToken(jdbcTemplate, frySess);
            userToCheckUuid = dbSession.getUuid();
        }

        /* get and prep user data */
        DbUser dbUser = UserController.getDbUserByUUID(jdbcTemplate, userToCheckUuid);
        dbUser.setPassword("SECRET");
        apiDatagram.setData(dbUser);

        return apiDatagram.success();
    }


    @PostMapping("/api/user/edit")
    @ResponseBody
    public String APIDbUserEdit (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();

        /* find user by session id unless its nil */
        if (frySess.equals("nil"))
            return apiDatagram.fail("No session");

        DbUser newUser = DbUser.fromJSON(body);
        DbUser requestingUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);

        if (!requestingUser.getType().equals(UserType.Admin))
        {   /* make sure edited user has valid fields */
            newUser.setUuid(requestingUser.getUuid());
            newUser.setIsGoogleAccount(requestingUser.getIsGoogleAccount());
            newUser.setType(requestingUser.getType());
            newUser.setMail(requestingUser.getMail());
            newUser.setPassword(requestingUser.getPassword());
        } /* otherwise its admin editing so don't care */
        else
        {   /* BUT HAVE TO copy over old password so it doesn't get lost */
            DbUser oldUserState = UserController.getDbUserByUUID(jdbcTemplate, newUser.getUuid());
            newUser.setPassword(oldUserState.getPassword());
        }

        /* update in db */
        UserController.updateUser(jdbcTemplate, newUser);
        return apiDatagram.success();
    }

    @PostMapping("/api/user/login")
    @ResponseBody
    public String APIDbUserLogin (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();
        DbUser wu = DbUser.fromJSON(body);
        /* confirm user exists */
        DbUser realUser = UserController.getDbUserByMail(jdbcTemplate, wu.getMail());
        if (realUser == null)
            return apiDatagram.fail("User with this mail does not exists!");

        /* confirm credentials */
        if (realUser.getPassword().equals(wu.getPassword()) == false)
            return apiDatagram.fail("Wrong password for this user");

        /* crate session for user */
        Timestamp expiration = new Timestamp(System.currentTimeMillis() + (1000L * 3600));
        DbSession dbSession = SessionController.newFromUserUUID(jdbcTemplate, realUser.getUuid(), expiration);
        ResponseCookie responseCookie = ResponseCookie.from("fry_sess", dbSession.getToken())
            .httpOnly(true)
            .sameSite("Strict")
            .secure(false)
            .path("/")
            .maxAge(Duration.ofHours(1))
            .build();
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        wu.setType(realUser.getType());
        apiDatagram.setData(wu);
        return apiDatagram.success();
    }

    @PostMapping("/api/user/password/set")
    @ResponseBody
    public String APIDbUserPasswordSet (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();
        /**
         * WARN: wu.mail is the old password for simplicity reason
         *       make sure to check if it matches old password
         *       before accepting and making changes
         */
        DbUser wu = DbUser.fromJSON(body);

        if (frySess.equals("nil"))
            return apiDatagram.fail("cookie hell");

        DbUser currentUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);

        if (currentUser == null)
            return apiDatagram.fail("użytkownik nie istnieje");

        if (!currentUser.getPassword().equals(wu.getMail()))
        {   /* boo hoo, passwords don't match */
            return apiDatagram.fail("hasło się nie zgadza");
        }

        /* set new password ! */
        currentUser.setPassword(wu.getPassword());
        UserController.updateUser(jdbcTemplate, currentUser);

        return apiDatagram.success();
    }

    @PostMapping("/api/user/logout")
    @ResponseBody
    public String APIDbUserLogout (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();

        /* remove sessions from db */
        SessionController.deleteByToken(jdbcTemplate, frySess);

        ResponseCookie responseCookie = ResponseCookie.from("fry_sess", "nil")
            .httpOnly(true)
            .sameSite("Strict")
            .secure(false)
            .path("/")
            .maxAge(0)
            .build();

        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return apiDatagram.success();
    }

    @PostMapping("/api/user/list")
    @ResponseBody
    public String APIDbUserList (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();

        if (frySess.equals("nil"))
            return apiDatagram.fail("No session");

        DbUser user = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
        if (!user.getType().equals(UserType.Admin))
            return apiDatagram.fail("No perms");

        List<DbUser> users = UserController.fetchAll(jdbcTemplate);
        users.forEach(u -> u.setPassword("")); /* securitas */

        apiDatagram.setData(users);
        return apiDatagram.success();
    }

    @SneakyThrows
    @PostMapping("/api/user/remove")
    @ResponseBody
    public String APIDbUserRemove (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();

        Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
        String uuid = (String) m.get("uuid");

        if (frySess.equals("nil"))
            return apiDatagram.fail("No session");

        DbUser user = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
        if (!user.getType().equals(UserType.Admin))
            return apiDatagram.fail("No perms");

        DbUser userToRemove = UserController.getDbUserByUUID(jdbcTemplate, uuid);
        if (userToRemove == null)
            return apiDatagram.fail("No user");

        if (!UserController.removeUser(jdbcTemplate, userToRemove))
            return apiDatagram.fail("Internal server err");

        return apiDatagram.success();
    }

}
