package pl.fryciarnia.orders;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.order.APIOrder;
import pl.fryciarnia.order.DbOrder;
import pl.fryciarnia.order.OrderController;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.recipe.DbRecipe;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserType;

import java.sql.Timestamp;
import java.util.ArrayList;
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
              dbOrders.getOrderStatus().ordinal(),
              dbOrders.getIsTakeout(),
              dbOrders.getUuid()
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

  public static List<DbOrders> getOrdersByHolding (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    List<DbOrders> dbOrdersList = fetchAll(jdbcTemplate);
    return dbOrdersList
        .stream()
        .filter(order -> order.getHolding().equals(dbHolding.getUuid()))
        .toList();
  }

  public static DbOrders getOrdersByUUID (JdbcTemplate jdbcTemplate, String uuid)
  {
    List<DbOrders> dbOrdersList = fetchAll(jdbcTemplate);
    for (DbOrders dbOrders : dbOrdersList)
      if (dbOrders.getUuid().equals(uuid))
        return dbOrders;
    return null;
  }

  public static List<AdpOrdersAdpOrderMealDbHolding> getAdpOrdersAdpOrderMealDbHolding (JdbcTemplate jdbcTemplate, List<DbOrders> ordersList)
  {
    ArrayList<AdpOrdersAdpOrderMealDbHolding> adpOrdersMeals = new ArrayList<>();

    ordersList.forEach(orders -> {
      AdpOrdersAdpOrderMealDbHolding adp = new AdpOrdersAdpOrderMealDbHolding();
      adp.setDbHolding(HoldingController.getHoldingByUUID(jdbcTemplate, orders.getHolding()));
      adp.setDbOrders(orders);
      List<DbOrder> lst = OrderController.getOrderByOrders(jdbcTemplate, orders);
      lst.forEach(order -> {
        DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, order.getMeal());
        AdpOrderMeal adpOrderMeal = new AdpOrderMeal();
        adpOrderMeal.setDbOrder(order);
        adpOrderMeal.setDbMeal(dbMeal);
        adp.getAdpOrderMeals().add(adpOrderMeal);
      });
      adpOrdersMeals.add(adp);
    });

    return adpOrdersMeals;
  }
  
  public static Float getPriceByDbOrders (JdbcTemplate jdbcTemplate, DbOrders dbOrders)
  {
    ArrayList<DbOrders> dbOrdersList = new ArrayList<>();
    dbOrdersList.add(dbOrders);
    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersAdpOrderMealDbHoldings = getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, dbOrdersList);
    AdpOrdersAdpOrderMealDbHolding adpOrdersAdpOrderMealDbHolding 
        = adpOrdersAdpOrderMealDbHoldings.get(0);

    /* finally, calculate price */
    return adpOrdersAdpOrderMealDbHolding.getAdpOrderMeals()
        .stream()
        .map((b) -> b.getDbMeal().getPrice() * Float.valueOf(b.getDbOrder().getQuantity()))
        .reduce(0.0f, (a, b) -> a + b);
  }

}
