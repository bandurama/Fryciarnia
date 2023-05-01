package pl.fryciarnia.recipe;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.ingridient.IngridientController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;

import java.util.ArrayList;
import java.util.List;

public class RecipeController
{
  public static boolean insertRecipe (JdbcTemplate jdbcTemplate, DbRecipe dbRecipe)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbRecipe VALUES(?, ?, ?, ?, ?, ?)",
              dbRecipe.getUuid(),
              dbRecipe.getMeal(),
              dbRecipe.getIngridient(),
              dbRecipe.getQuantity(),
              dbRecipe.getInstruction(),
              dbRecipe.getStep()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbRecipe> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DbRecipe",
            BeanPropertyRowMapper.newInstance(DbRecipe.class)
        );
  }

  public static DbRecipe getRecipeByUUID (JdbcTemplate jdbcTemplate, String uuid)
  {
    List<DbRecipe> dbRecipeList = fetchAll(jdbcTemplate);
    for (DbRecipe dbRecipe : dbRecipeList)
      if (dbRecipe.getUuid().equals(uuid))
        return dbRecipe;
    return null;
  }

  public static List<DbRecipe> getRecipesByMeal (JdbcTemplate jdbcTemplate, DbMeal dbMeal)
  {
    return fetchAll(jdbcTemplate)
        .stream()
        .filter(dbRecipe -> dbRecipe.getMeal().equals(dbMeal.getUuid()))
        .toList();
  }

  public static boolean removeRecipe (JdbcTemplate jdbcTemplate, DbRecipe dbRecipe)
  {
    try
    {
      jdbcTemplate.update ("DELETE FROM DBRECIPE WHERE UUID = ?", new Object [] { dbRecipe.getUuid() });
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<AdpRecipeIngridient> getRecipeIngridientByDbMeal (JdbcTemplate jdbcTemplate, DbMeal dbMeal)
  {
    List<DbRecipe> dbRecipeList = RecipeController.getRecipesByMeal(jdbcTemplate, dbMeal);
    List<AdpRecipeIngridient> adpRecipeIngridients = new ArrayList<>();

    for (DbRecipe dbRecipe : dbRecipeList)
    {
      DbIngridient dbIngridient = IngridientController.getIngridientByUUID(jdbcTemplate, dbRecipe.getIngridient());
      if (dbIngridient == null)
        return null;

      AdpRecipeIngridient adpRecipeIngridient = new AdpRecipeIngridient();
      adpRecipeIngridient.setDbRecipe(dbRecipe);
      adpRecipeIngridient.setDbIngridient(dbIngridient);
      adpRecipeIngridients.add(adpRecipeIngridient);
    }

    return adpRecipeIngridients;
  }
}
