package pl.fryciarnia.meal;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.utils.APIDatagram;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class MealMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/meal/list")
  @ResponseBody
  public String APIDbMealList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    List<DbMeal> dbMealList = MealController.fetchAll(jdbcTemplate);
    apiDatagram.setData(dbMealList);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/meal/info")
  @ResponseBody
  public String APIDbMealInfo (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    /**
     *
     * HACK: Create this adapter in RecipeController
     *       cause it does matches syntactic style
     *       of this project.
     */
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");
    DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, uuid);

    if (dbMeal == null)
      return apiDatagram.fail("Meal does not exists");

    apiDatagram.setData(dbMeal);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/meal/remove")
  @ResponseBody
  public String APIDbMealRemove (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");
    DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, uuid);

    if (dbMeal == null)
      return apiDatagram.fail("Meal does not exists");

    if (!MealController.removeMeal(jdbcTemplate, dbMeal))
      return apiDatagram.fail("Internal server error");

    return apiDatagram.success();
  }


  @SneakyThrows
  @PostMapping("/api/meal/insert")
  @ResponseBody
  public String APIDbMealInsert (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    DbMeal dbMeal = DbMeal.fromJSON(body);
    if (dbMeal == null)
      return apiDatagram.fail("Błędne dane");
    dbMeal.setUuid(UUID.randomUUID().toString());

    if (!MealController.insertMeal(jdbcTemplate, dbMeal))
      return apiDatagram.fail("Internal server error");

    apiDatagram.setData(dbMeal);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/meal/edit")
  @ResponseBody
  public String APIDbMealEdit (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    DbMeal newMeal = DbMeal.fromJSON(body);
    if (newMeal == null)
      return apiDatagram.fail("Błędne dane");
//    System.out.println(newMeal);

    if (!MealController.updateMeal(jdbcTemplate, newMeal))
      return apiDatagram.fail("Internal server error");

    apiDatagram.setData(newMeal);
    return apiDatagram.success();
  }

}
