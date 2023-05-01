package pl.fryciarnia.meal;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.ingridient.DbIngridient;

import java.util.List;

public class MealController
{
  public static boolean insertMeal(JdbcTemplate jdbcTemplate, DbMeal dbMeal)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbMeal VALUES(?, ?, ?, ?, ?, ?)",
              dbMeal.getUuid(),
              dbMeal.getName(),
              dbMeal.getPrice(),
              dbMeal.getImage(),
              dbMeal.getIcon(),
              dbMeal.getIsListed()
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

  public static DbMeal getMealByUUID (JdbcTemplate jdbcTemplate, String uuid)
  {
    List<DbMeal> dbMealList = fetchAll(jdbcTemplate);
    for (DbMeal dbMeal : dbMealList)
      if (dbMeal.getUuid().equals(uuid))
        return dbMeal;
    return null;
  }

  public static boolean removeMeal (JdbcTemplate jdbcTemplate, DbMeal dbMeal)
  {
    try
    {
      jdbcTemplate.update ("UPDATE DBMEAL SET ISLISTED = 0 WHERE UUID = ?", new Object [] { dbMeal.getUuid() });
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static boolean updateMeal (JdbcTemplate jdbcTemplate, DbMeal dbMeal)
  {
    try
    {
      jdbcTemplate.update
          (
              "UPDATE DBMEAL SET NAME = ?, PRICE = ?, IMAGE = ?, ICON = ?, ISLISTED = ? WHERE UUID = ?",
              dbMeal.getName(),
              dbMeal.getPrice(),
              dbMeal.getImage(),
              dbMeal.getIcon(),
              dbMeal.getIsListed(),
              dbMeal.getUuid()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }
}