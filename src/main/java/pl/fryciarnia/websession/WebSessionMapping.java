package pl.fryciarnia.websession;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.Temporal;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class WebSessionMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/websession/google/login")
  @ResponseBody
  public String mappingApi (@RequestBody String body, HttpServletResponse httpServletResponse)
  {
    /* do the google magic */
    WebSession reqBody = WebSession.fromJSON(body);
    WebSession newSession = WebSessionController.newFromGoogleAccessToken(jdbcTemplate, reqBody.getToken());

    /* bail if failed */
    if (newSession == null)
      return "{}";

    /* create session cookie */
    Long currentTimeStamp = System.currentTimeMillis();
    Long expiration = newSession.getExpiration().getTime();

    ResponseCookie responseCookie = ResponseCookie.from("fry_sess", newSession.getToken())
			.httpOnly(true)
			.sameSite("Strict")
			.secure(false)
			.maxAge(expiration - currentTimeStamp)
			.build();

    httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

    /* return info to client */
    return (new Gson()).toJson(newSession);
  }

}
