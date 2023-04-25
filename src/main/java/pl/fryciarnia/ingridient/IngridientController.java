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
}
