package pl.fryciarnia.orders;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.order.APIOrder;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.recipe.DbRecipe;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserType;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

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

  public static boolean updateOrders (JdbcTemplate jdbcTemplate, DbOrders dbOrders)
  {
    try
    {
      jdbcTemplate.update
          (
              "UPDATE DBORDERS SET TICKET = ?, CTIME = ?, OWNER = ?, HOLDING = ?, " +
                  "                    ORDERSTATUS = ?, ISTAKEOUT = ? WHERE UUID = ?",
              dbOrders.getTicket(),
              dbOrders.getCtime(),
              dbOrders.getOwner(),
              dbOrders.getHolding(),
              dbOrders.getOrderStatus(),
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

  public static boolean removeOrders (JdbcTemplate jdbcTemplate, DbOrders dbOrders)
  {
    try
    {
      jdbcTemplate.update ("DELETE FROM DBORDER WHERE ORIGIN = ?", new Object [] { dbOrders.getUuid() });
      jdbcTemplate.update ("DELETE FROM DBORDERS WHERE UUID = ?", new Object [] { dbOrders.getUuid() });
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static DbOrders newDbOrdersFromAPIOrder (JdbcTemplate jdbcTemplate, APIOrder apiOrder, DbUser dbUser)
  {
    DbOrders dbOrders = new DbOrders();
    dbOrders.setUuid(UUID.randomUUID().toString());
    dbOrders.setOwner(dbUser.getUuid());
    dbOrders.setHolding(apiOrder.getHoldingUUID());
    dbOrders.setIsTakeout(dbUser.getType().equals(UserType.Web));
    dbOrders.setOrderStatus(OrderStatus.PAYING);
    dbOrders.setTicket(0 /* TODO: Create Ticket Controller */);
    dbOrders.setCtime(new Timestamp(System.currentTimeMillis()));

    if (!insertOrders(jdbcTemplate, dbOrders))
      return null;

    return dbOrders;
  }
}
