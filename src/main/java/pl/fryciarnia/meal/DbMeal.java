package pl.fryciarnia.meal;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import pl.fryciarnia.ingridient.DbIngridient;

import java.util.Map;

/**
 * Posiłek / Danie / Oferta ogólna dla
 * wszystkich lokali.
 */
@Data
public class DbMeal
{
  private String uuid;
  private String name;
  private float price;

  @SneakyThrows
  public static DbMeal fromJSON (String json)
  {
    DbMeal self = new DbMeal();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

    self.setUuid((String) m.get("uuid"));
    self.setName((String) m.get("name"));
    self.setPrice((float) m.get("price"));

    return self;
  }
}
