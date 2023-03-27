package pl.fryciarnia.websession;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.utils.APIDatagram;

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
    /* formal response */
    APIDatagram apiDatagram = new APIDatagram();

    /* do the google magic */
    WebSession reqBody = WebSession.fromJSON(body);
    WebSession newSession = WebSessionController.newFromGoogleAccessToken(jdbcTemplate, reqBody.getToken());

    /* bail if failed */
    if (newSession == null)
    {
      apiDatagram.setOk(false);
      apiDatagram.setMsg("Tworzenie sesji z Google OAuth nie powidoło się");
      return apiDatagram.toString();
    }

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
    apiDatagram.setOk(true);
    apiDatagram.setData(newSession);
    return apiDatagram.toString();
  }

}
