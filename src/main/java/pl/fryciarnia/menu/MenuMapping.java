package pl.fryciarnia.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.recipe.RecipeController;
import pl.fryciarnia.utils.APIDatagram;

import java.util.List;
import java.util.Map;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class MenuMapping
{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @SneakyThrows
  @PostMapping("/api/menu")
  @ResponseBody
  public String APIMenu (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    /**
     * WANR: This is different in retrospect to the
     * /api/meal/list because it has the capability
     * to filter out items that are not currently
     * available in the holding.
     * If holding uuid is not given, then filtering
     * is omitted, and it acts just like that other
     * api hook.
     */
    APIDatagram apiDatagram = new APIDatagram();
    List<DbMeal> meals = MealController.fetchAll(jdbcTemplate);
    meals = meals.stream().filter(dbMeal -> dbMeal.getIsListed()).toList();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);

    if (m.containsKey("uuid"))
    { /* if uuid was given, filter things out */
      DbHolding dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, (String) m.get("uuid"));
      if (dbHolding == null)
        return apiDatagram.fail("holding given but does not exists");
      meals = MenuController.getAvailableMealsForHolding(jdbcTemplate, meals, dbHolding);
    }

    apiDatagram.setData(meals);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/menu/meal")
  @ResponseBody
  public String APIMenuMeal (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);

    if (!m.containsKey("uuid"))
      return apiDatagram.fail("No uuid given");

    DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, (String) m.get("uuid"));
    if (dbMeal == null)
      apiDatagram.fail("Meal does not exists");

    AdpMealAdpRecipeIngridient adpMealAdpRecipeIngridient = new AdpMealAdpRecipeIngridient();
    adpMealAdpRecipeIngridient.setAdpRecipeIngridient(
        RecipeController.getRecipeIngridientByDbMeal(jdbcTemplate, dbMeal));
    adpMealAdpRecipeIngridient.setDbMeal(dbMeal);

    apiDatagram.setData(adpMealAdpRecipeIngridient);
    return apiDatagram.success();
  }
}
