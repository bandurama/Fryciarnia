package pl.fryciarnia.websession;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import pl.fryciarnia.webuser.WebUser;
import pl.fryciarnia.webuser.WebUserController;
import pl.fryciarnia.webuser.WebUserType;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebSessionController
{
  private static final String GOOGLE_API_ACCESS_TOKEN_VERIFY = "https://www.googleapis.com/oauth2/v2/tokeninfo";


  public static List<WebSession> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query("SELECT * FROM WEBSSESION", BeanPropertyRowMapper.newInstance(WebSession.class));
  }

  public static WebSession getWebSessionByToken (JdbcTemplate jdbcTemplate, String token)
  {
    List<WebSession> all = fetchAll(jdbcTemplate);
    for (WebSession w : all)
      if (w.getToken().equals(token))
        return w;
    return null;
  }

  public static WebSession newFromUserUUID (JdbcTemplate jdbcTemplate, String uuid, Timestamp expiration)
  {
    /* create new session */
    WebSession webSession = new WebSession();
    webSession.setExpiration(expiration);
    webSession.setUuid(uuid);
    webSession.setToken(String.valueOf(UUID.randomUUID()));

    /* insert websession to database */
    jdbcTemplate.update
		(
				"INSERT INTO WEBSESSION VALUES (?, ?, ?)",
				webSession.getToken(),
				webSession.getExpiration(),
				webSession.getUuid()
		);

    return webSession;
  }

  /**
   * Utworzenie sesji ORAZ możliwe że też nowego konta użytkownika
   * z zaznaczoną flagą isGoogleAccount
   * @param jdbcTemplate
   * @return model nowej sesji
   */
  public static WebSession newFromGoogleAccessToken (JdbcTemplate jdbcTemplate, String googleAccessToken)
  {
    /* confirm access_token is valid */
    String verificationAPIHook = GOOGLE_API_ACCESS_TOKEN_VERIFY + "?access_token=" + googleAccessToken;
    RestTemplate restTemplate = new RestTemplate();
    try
    {
      /* perform call to Google API */
      String response = restTemplate.getForObject(verificationAPIHook, String.class);
      Map<String, Object> responseMap = (new ObjectMapper()).readValue(response, Map.class);

      /* make sure all required fields are present */
      if (!responseMap.containsKey("user_id"))
        throw new Exception("Google API Endpoind could not provide user_id field");

      if (!responseMap.containsKey("expires_in"))
        throw new Exception("Google API Endpoind could not provide expires_in field");

      /* Either register or login user */
      String googleUserId = (String) responseMap.get("user_id");
      WebUser testUser = WebUserController.getWebUserByUUID(jdbcTemplate, googleUserId);

      /* If user DOES NOT exists */
      if (testUser == null)
      {
        /* Register 'em using either mail or google id */
        WebUser webUser = new WebUser();
        webUser.setType(WebUserType.Web);
        webUser.setPassword("NONE");
        webUser.setIsGoogleAccount(true);
        webUser.setMail(responseMap.containsKey("email")
					? (String) responseMap.get("email")
					: googleUserId);

        webUser.setUuid(googleUserId);
        WebUserController.insertUser(jdbcTemplate, webUser);
      }

      /* create session for given googleUserId user uuid */
      Integer googleExpiration = (Integer) responseMap.get("expires_in");
      Timestamp expiration = new Timestamp(System.currentTimeMillis() + 1000L * googleExpiration);
      return WebSessionController.newFromUserUUID(jdbcTemplate, googleUserId, expiration);
    }
    catch (Exception e)
    {
      System.out.println(e);
    }

    return null;
  }

}
