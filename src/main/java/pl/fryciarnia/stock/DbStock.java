package pl.fryciarnia.stock;

import lombok.Data;
import lombok.SneakyThrows;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Składnik przetrzymywany przez konkretny lokal,
 * system pilnuje zużytej ilości.
 */
@Data
public class DbStock
{
  private String uuid;
  private String holding;
  private String ingridient;
  private Float quantity;

  @SneakyThrows
  public static DbStock fromJSON (String json)
  {
    DbStock self = new DbStock();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);
    self.setUuid((String) m.get("uuid"));
    self.setIngridient((String) m.get("ingridient"));
    self.setHolding((String) m.get("holding"));
    self.setQuantity(Float.valueOf((String) m.get("quantity")));
    return self;
  }
}
