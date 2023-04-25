package pl.fryciarnia.ingridient;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;

import java.util.List;

public class IngridientController
{
  public static boolean insertIngridient (JdbcTemplate jdbcTemplate, DbIngridient dbIngridient)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbIngridient VALUES(?, ?)",
              dbIngridient.getUuid(),
              dbIngridient.getName()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbIngridient> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DBINGRIDIENT",
            BeanPropertyRowMapper.newInstance(DbIngridient.class)
        );
  }

  public static DbIngridient getIngridientByUUID (JdbcTemplate jdbcTemplate, String uuid)
  {
    List<DbIngridient> ingridientList = fetchAll(jdbcTemplate);
    for (DbIngridient i : ingridientList)
      if (i.getUuid().equals(uuid))
        return i;
    return null;
  }

  public static boolean removeIngridient (JdbcTemplate jdbcTemplate, DbIngridient ingridient)
  {
    try
    {
      jdbcTemplate.update ("DELETE FROM DBINGRIDIENTS WHERE INGRIDIENT = ?", new Object [] { ingridient.getUuid() });
      jdbcTemplate.update ("DELETE FROM DBRECIPE WHERE INGRIDIENT = ?", new Object [] { ingridient.getUuid() });
      jdbcTemplate.update ("DELETE FROM DBINGRIDIENT WHERE UUID = ?", new Object [] { ingridient.getUuid() });
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static boolean updateIngridient (JdbcTemplate jdbcTemplate, DbIngridient dbIngridient)
  {
    try
    {
      jdbcTemplate.update
          (
              "UPDATE DBINGRIDIENT SET NAME = ? WHERE UUID = ?",
              dbIngridient.getName(),
              dbIngridient.getUuid()
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
