package pl.fryciarnia.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * Element pojedyńczego zamówenia - tzn. jeden
 * posiłek z całego zamówienia. Flaga isReady
 * dla ekranu wyświetlającego kolejkę.
 */
@Data
public class DbOrder
{
  private String origin;
  private String meal;

  @SneakyThrows
  public static DbOrder fromJSON (String json)
  {
    DbOrder self = new DbOrder();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

    self.setOrigin((String) m.get("origin"));
    self.setMeal((String) m.get("meal"));

    return self;
  }
}
