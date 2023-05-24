package pl.fryciarnia.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

  public static DbRecipe fromJSON (String json)
  {
    try
    {
      return (new Gson()).fromJson(json, DbRecipe.class);
    }
    catch (Exception e)
    {
      return null;
    }
  }
}
