package pl.fryciarnia.meal;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MealController
{
  public static boolean insertMeal(JdbcTemplate jdbcTemplate, DbMeal dbMeal)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbMeal VALUES(?, ?, ?)",
              dbMeal.getUuid(),
              dbMeal.getName(),
              dbMeal.getPrice()
          );
    } catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbMeal> fetchAll(JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DBMEAL",
            BeanPropertyRowMapper.newInstance(DbMeal.class)
        );
  }
}