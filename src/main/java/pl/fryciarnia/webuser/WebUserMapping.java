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
    public String APIWebUserRegister (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "testCookie", defaultValue = "ERR") String testCookieRed)
    {
        System.out.println("RED COOKIE " + testCookieRed);
        WebUser wu = WebUser.fromJSON(body);

        /* create new uuid */
        wu.setUuid(String.valueOf(UUID.randomUUID()));

        /* validate rest of fields */
        wu.setSuccess(true);

        if (wu.getMail() == null)
        {
            wu.setSuccess(false);
            wu.setMsg("Błędny format mail'a");
        }

        if (wu.getPassword() == null)
        {
            wu.setSuccess(false);
            wu.setMsg("Błędne hasło");
        }

        if (wu.getType() == null)
        {
            wu.setSuccess(false);
            wu.setMsg("Błędny typ");
        }

        /* if all checks succeed, create new instance of WebUser in DB */
        if (wu.getSuccess())
        {
            if (!WebUserController.insertUser(jdbcTemplate, wu))
            { /* insert failed */
                wu.setSuccess(false);
                wu.setMsg("db insert fail");
            }
        }

        /* return object as response */
//        Cookie testCookie = new Cookie("testCookie", "Jebacdisa2137");
//        testCookie.setMaxAge(7 * 24 * 60 * 60);
//        testCookie.setDomain("http://bandurama.ddns.net");
//        testCookie.setSecure(true);
//        testCookie.setHttpOnly(true);
//        httpServletResponse.addCookie(testCookie);



        ResponseCookie responseCookie = ResponseCookie.from("testCookie", wu.getUuid())
            .httpOnly(true)
            .sameSite("Strict")
            .secure(false)
            .maxAge(Duration.ofHours(2))
            .build();
        httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());


        return new Gson().toJson(wu);
    }
}
