package pl.fryciarnia.meal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
  private String image;
  private String icon;
  private Boolean isListed;

  public static DbMeal fromJSON (String json)
  {
    try
    {
      return (new Gson()).fromJson(json, DbMeal.class);
    }
    catch (Exception e)
    {
      return null;
    }
  }
}
