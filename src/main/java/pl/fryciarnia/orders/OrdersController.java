package pl.fryciarnia.orders;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.recipe.DbRecipe;

import java.util.List;

public class OrdersController
{
  public static boolean insertOrders (JdbcTemplate jdbcTemplate, DbOrders dbOrders)
  {

    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbOrders VALUES(?, ?, ?, ?, ?, ?, ?)",
              dbOrders.getUuid(),
              dbOrders.getTicket(),
              dbOrders.getCtime(),
              dbOrders.getOwner(),
              dbOrders.getHolding(),
              dbOrders.getOrderStatus().ordinal(),
              dbOrders.getIsTakeout()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbOrders> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DbOrders",
            BeanPropertyRowMapper.newInstance(DbOrders.class)
        );
  }
}
