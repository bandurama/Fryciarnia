package pl.fryciarnia.webuser;

import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.utils.APIDatagram;

import java.time.Duration;
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class WebUserMapping
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/api/webuser/register")
    @ResponseBody
    public String APIWebUserRegister (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "testCookie", defaultValue = "") String testCookieRed)
    {
//        System.out.println("RED COOKIE " + testCookieRed);
        APIDatagram apiDatagram = new APIDatagram();
        WebUser wu = WebUser.fromJSON(body);

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

        /* if all checks succeed, create new instance of WebUser in DB */
        if (apiDatagram.isOk())
        {
            if (!WebUserController.insertUser(jdbcTemplate, wu))
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
