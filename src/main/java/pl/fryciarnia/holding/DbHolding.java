package pl.fryciarnia.holding;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * Reprezentacja pojedyńczego lokalu udostępnionego
 * we franczyzie.
 */
@Data
public class DbHolding
{
  private String uuid;
  private String localization;
  private String manager;

  @SneakyThrows
  public static DbHolding fromJSON (String json)
  {
    DbHolding self = new DbHolding();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);
    self.setLocalization((String) m.get("localization"));
    self.setManager((String) m.get("manager"));
    self.setUuid((String) m.get("uuid"));
    return self;
  }
}
