package pl.fryciarnia.session;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.utils.APIDatagram;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class SessionMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/session/google/login")
  @ResponseBody
  public String mappingApi (@RequestBody String body, HttpServletResponse httpServletResponse)
  {
    /* formal response */
    APIDatagram apiDatagram = new APIDatagram();

    /* do the google magic */
    DbSession reqBody = DbSession.fromJSON(body);
    DbSession newDbSession = SessionController.newFromGoogleAccessToken(jdbcTemplate, reqBody.getToken());

    /* bail if failed */
    if (newDbSession == null)
    {
      apiDatagram.setOk(false);
      apiDatagram.setMsg("Tworzenie sesji z Google OAuth nie powidoło się");
      return apiDatagram.toString();
    }

    /* create session cookie */
    Long currentTimeStamp = System.currentTimeMillis();
    Long expiration = newDbSession.getExpiration().getTime();

    ResponseCookie responseCookie = ResponseCookie.from("fry_sess", newDbSession.getToken())
			.httpOnly(true)
			.sameSite("Strict")
			.secure(false)
			.maxAge(expiration - currentTimeStamp)
			.build();

    httpServletResponse.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

    /* return info to client */
    apiDatagram.setOk(true);
    apiDatagram.setData(newDbSession);
    return apiDatagram.toString();
  }

}
