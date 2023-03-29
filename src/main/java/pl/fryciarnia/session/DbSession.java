package pl.fryciarnia.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.util.Map;

/**
 * WebSession reprezentuje otwarą sesję na serwerze
 * czyli zalogowanego użytkownika, w przypadku
 * użytkowinków social-media, konto tworzone jest
 * automatycznie na podstawie informacji z google
 */
@Data
public class DbSession
{
  private String uuid;          /* User uuid */
  private Timestamp expiration; /* Time to live */
  private String token;         /* Stored in cookies */

  @SneakyThrows
  public static DbSession fromJSON (String json)
  {
    DbSession s = new DbSession();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);
    s.setUuid((String) m.get("uuid"));
    s.setExpiration((Timestamp) m.get("expiration"));
    s.setToken((String) m.get("token"));
    return s;
  }
}
