package pl.fryciarnia.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import pl.fryciarnia.order.DbOrder;

import java.util.Map;

/**
 * Jeden z korków przepisu na dany posiłek.
 * Oprócz instrukcji dla pracowników zawiera
 * informacje potrzebne dla systemu
 * zarządzania kuchnią, systemu sprzedaży
 * itp...
 */
@Data
public class DbRecipe
{
  private String uuid;
  private String meal;
  private String ingridient;
  private Float quantity;
  private String instruction;
  private Integer step;

  @SneakyThrows
  public static DbRecipe fromJSON (String json)
  {
    DbRecipe self = new DbRecipe();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

    self.setMeal((String) m.get("uuid"));
    self.setMeal((String) m.get("meal"));
    self.setIngridient((String) m.get("ingridient"));
    self.setQuantity(Float.valueOf((String) m.get("quantity")));
    self.setInstruction((String) m.get("instruction"));
    self.setStep(Integer.valueOf((String) m.get("step")));

    return self;
  }
}
