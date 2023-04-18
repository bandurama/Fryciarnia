package pl.fryciarnia.ingridients;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.ingridient.DbIngridient;

import java.util.List;

public class IngridientsController
{
  public static boolean insertIngridients (JdbcTemplate jdbcTemplate, DbIngridients dbIngridients)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbIngridients VALUES(?, ?, ?)",
              dbIngridients.getHolding(),
              dbIngridients.getIngridient(),
              dbIngridients.getQuantity()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbIngridients> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DBINGRIDIENTS",
            BeanPropertyRowMapper.newInstance(DbIngridients.class)
        );
  }
}
