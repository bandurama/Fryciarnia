package pl.fryciarnia.order;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class OrderController
{
  public static boolean insertOrder (JdbcTemplate jdbcTemplate, DbOrder dbOrder)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbOrder VALUES(?, ?)",
              dbOrder.getOrigin(),
              dbOrder.getMeal()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbOrder> fetchAll(JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DbOrder",
            BeanPropertyRowMapper.newInstance(DbOrder.class)
        );
  }
}
