package pl.fryciarnia.ingridient;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import pl.fryciarnia.holding.DbHolding;

import java.util.Map;

/**
 * Pojedyńczy składnik przepisu, ogólny dla
 * każdego lokalu.
 */
@Data
public class DbIngridient
{
  private String uuid;
  private String name;
  private String icon;

  @SneakyThrows
  public static DbIngridient fromJSON (String json)
  {
    DbIngridient self = new DbIngridient();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

    self.setUuid((String) m.get("uuid"));
    self.setName((String) m.get("name"));
    self.setIcon((String) m.get("icon"));

    return self;
  }
}
