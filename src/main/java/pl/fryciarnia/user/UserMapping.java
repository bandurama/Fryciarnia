package pl.fryciarnia.user;

import jakarta.servlet.http.HttpServletResponse;
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

        /* if all checked, create new session for this user */
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

        apiDatagram.setData(wu);
        return apiDatagram.toString();
    }

    @PostMapping("/api/user/info")
    @ResponseBody
    public String APIDbUserInfo (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
        APIDatagram apiDatagram = new APIDatagram();

        /* find user by session id unless its nil */
        if (frySess.equals("nil"))
            return apiDatagram.fail("No session");

        DbSession dbSession = SessionController.getSessionByToken(jdbcTemplate, frySess);
        DbUser dbUser = UserController.getDbUserByUUID(jdbcTemplate, dbSession.getUuid());

        /* too sensitive */
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

        DbSession dbSession = SessionController.getSessionByToken(jdbcTemplate, frySess);
        DbUser dbUser = UserController.getDbUserByUUID(jdbcTemplate, dbSession.getUuid());

        /* user info from website */
        DbUser diffUser = DbUser.fromJSON(body);

        /* update only certain field in final user and tell to update */
        dbUser.setName(diffUser.getName());
        dbUser.setSurname(diffUser.getSurname());

        /* update in db */
        UserController.updateUser(jdbcTemplate, dbUser);

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

}
