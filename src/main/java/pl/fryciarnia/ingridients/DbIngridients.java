package pl.fryciarnia.ingridients;

import lombok.Data;
import lombok.SneakyThrows;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Składnik przetrzymywany przez konkretny lokal,
 * system pilnuje zużytej ilości.
 */
@Data
public class DbIngridients
{
  private String holding;
  private String ingridient;
  private Float quantity;

  @SneakyThrows
  public static DbIngridients fromJSON (String json)
  {
    DbIngridients self = new DbIngridients();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);
    self.setIngridient((String) m.get("ingridient"));
    self.setHolding((String) m.get("holding"));
    self.setQuantity(Float.valueOf((String) m.get("quantity")));
    return self;
  }
}
