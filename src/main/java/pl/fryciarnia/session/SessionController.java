package pl.fryciarnia.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SessionController
{
  private static final String GOOGLE_API_ACCESS_TOKEN_VERIFY = "https://www.googleapis.com/oauth2/v2/tokeninfo";


  public static List<DbSession> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query("SELECT * FROM DBSESSION", BeanPropertyRowMapper.newInstance(DbSession.class));
  }

  public static DbSession getSessionByToken(JdbcTemplate jdbcTemplate, String token)
  {
    List<DbSession> all = fetchAll(jdbcTemplate);
    for (DbSession w : all)
      if (w.getToken().equals(token))
        return w;
    return null;
  }

  public static DbSession newFromUserUUID (JdbcTemplate jdbcTemplate, String uuid, Timestamp expiration)
  {
    /* create new session */
    DbSession dbSession = new DbSession();
    dbSession.setExpiration(expiration);
    dbSession.setUuid(uuid);
    dbSession.setToken(String.valueOf(UUID.randomUUID()));

    /* insert websession to database */
    jdbcTemplate.update
		(
				"INSERT INTO DBSESSION VALUES (?, ?, ?)",
				dbSession.getToken(),
				dbSession.getExpiration(),
				dbSession.getUuid()
		);

    return dbSession;
  }

  /**
   * Utworzenie sesji ORAZ możliwe że też nowego konta użytkownika
   * z zaznaczoną flagą isGoogleAccount
   * @param jdbcTemplate
   * @return model nowej sesji
   */
  public static DbSession newFromGoogleAccessToken (JdbcTemplate jdbcTemplate, String googleAccessToken)
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
      DbUser testUser = UserController.getDbUserByUUID(jdbcTemplate, googleUserId);

      /* If user DOES NOT exists */
      if (testUser == null)
      {
        /* Register 'em using either mail or google id */
        DbUser webUser = new DbUser();
        webUser.setType(UserType.Web);
        webUser.setPassword("NONE");
        webUser.setIsGoogleAccount(true);
        webUser.setMail(responseMap.containsKey("email")
					? (String) responseMap.get("email")
					: googleUserId);

        webUser.setUuid(googleUserId);
        UserController.insertUser(jdbcTemplate, webUser);
      }

      /* create session for given googleUserId user uuid */
      Integer googleExpiration = (Integer) responseMap.get("expires_in");
      Timestamp expiration = new Timestamp(System.currentTimeMillis() + 1000L * googleExpiration);
      return SessionController.newFromUserUUID(jdbcTemplate, googleUserId, expiration);
    }
    catch (Exception e)
    {
      System.out.println(e);
    }

    return null;
  }

}
