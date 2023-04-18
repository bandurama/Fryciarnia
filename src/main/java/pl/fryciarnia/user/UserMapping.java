package pl.fryciarnia.user;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.utils.APIDatagram;

import java.time.Duration;
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class UserMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/api/user/ping")
    public String APIDbUserPing (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
    {
//        ResponseCookie responseCookie = ResponseCookie.from("fry_sess", "HelloWorld")
//            .httpOnly(true)
//            .sameSite("Strict")
//            .secure(false)
//            .maxAge(Duration.ofHours(2))
//            .path("/")
//            .build();
//        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return "SESSION KEY IS: " + frySess;
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

        ResponseCookie responseCookie = ResponseCookie.from("testCookie", wu.getUuid())
            .httpOnly(true)
            .sameSite("Strict")
            .secure(false)
            .maxAge(Duration.ofHours(2))
            .build();
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        apiDatagram.setData(wu);
        return apiDatagram.toString();
    }
}
