package pl.fryciarnia.recipe;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.ingridient.DbIngridient;

import java.util.List;

public class RecipeController
{
  public static boolean insertRecipe (JdbcTemplate jdbcTemplate, DbRecipe dbRecipe)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbRecipe VALUES(?, ?, ?, ?, ?)",
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
}
