package pl.fryciarnia.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.ingridient.IngridientController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class RecipeMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * WARN: REQUIRES MEAL UUID TO BE GIVEN AS {uuid: ...}
   *
   * @param httpServletResponse
   * @param frySess
   * @return
   */
  @SneakyThrows
  @PostMapping("/api/recipe/list")
  @ResponseBody
  public String APIDbRecipeList(@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");
    DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, uuid);

    List<DbRecipe> dbRecipeList = RecipeController.getRecipesByMeal(jdbcTemplate, dbMeal);
    List<AdpRecipeIngridient> adpRecipeIngridients = new ArrayList<>();

    for (DbRecipe dbRecipe : dbRecipeList)
    {
      DbIngridient dbIngridient = IngridientController.getIngridientByUUID(jdbcTemplate, dbRecipe.getIngridient());
      if (dbIngridient == null)
        return apiDatagram.fail("Weird error, cannot create AdpRecipeIngridient cause DbIngridient is NULL");
      AdpRecipeIngridient adpRecipeIngridient = new AdpRecipeIngridient();
      adpRecipeIngridient.setDbRecipe(dbRecipe);
      adpRecipeIngridient.setDbIngridient(dbIngridient);
      adpRecipeIngridients.add(adpRecipeIngridient);
    }

    apiDatagram.setData(adpRecipeIngridients);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/recipe/insert")
  @ResponseBody
  public String APIDbRecipeInsert(@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (sessionUser == null || !sessionUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("Invalid session");

    DbRecipe dbRecipe = DbRecipe.fromJSON(body);
    dbRecipe.setUuid(UUID.randomUUID().toString());
    if (!RecipeController.insertRecipe(jdbcTemplate, dbRecipe))
      return apiDatagram.fail("Internal server error");

    apiDatagram.setData(dbRecipe);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/recipe/remove")
  @ResponseBody
  public String APIDbMealRemove (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");
    DbRecipe dbRecipe = RecipeController.getRecipeByUUID(jdbcTemplate, uuid);

    if (dbRecipe == null)
      return apiDatagram.fail("Meal does not exists");

    if (!RecipeController.removeRecipe(jdbcTemplate, dbRecipe))
      return apiDatagram.fail("Internal server error");

    return apiDatagram.success();
  }
}