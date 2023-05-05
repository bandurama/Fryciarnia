package pl.fryciarnia.order;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.orders.DbOrders;
import pl.fryciarnia.orders.OrdersController;
import pl.fryciarnia.recipe.DbRecipe;
import pl.fryciarnia.recipe.RecipeController;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.stock.StockController;
import pl.fryciarnia.user.DbUser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderController
{
  public static boolean insertOrder (JdbcTemplate jdbcTemplate, DbOrder dbOrder)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbOrder VALUES(?, ?, ?)",
              dbOrder.getOrigin(),
              dbOrder.getMeal(),
              dbOrder.getQuantity()
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

  public static List<DbOrder> getOrderByOrders (JdbcTemplate jdbcTemplate, DbOrders dbOrders)
  {
    return fetchAll(jdbcTemplate)
        .stream()
        .filter(dbOrder -> dbOrder.getOrigin().equals(dbOrders.getUuid()))
        .toList();
  }

  public static boolean canExecuteOrder (JdbcTemplate jdbcTemplate, APIOrder order)
  {
    DbHolding dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, order.getHoldingUUID());
    List<DbStock> dbStockList = StockController.getStockByHolding(jdbcTemplate, dbHolding);
    /* HACK: convert to HashMap for faster look up times */
    HashMap<String, DbStock> stock = new HashMap<>();
    for (DbStock dbStock : dbStockList)
      stock.put(dbStock.getIngridient(), dbStock);

    for (APIOrderedMeal orderedMeal : order.getOrderedMeals())
    { /* confirm it is definitively possible to cook this meal */
      DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, orderedMeal.getMealUUID());
      List<DbRecipe> recipe = RecipeController.getRecipesByMeal(jdbcTemplate, dbMeal);
      /* check each step and adjust for quantities */
      for (DbRecipe step : recipe)
      {
        if (!stock.containsKey(step.getIngridient()))
          return false; /* weird bug */

        DbStock pos = stock.get(step.getIngridient());
        if (pos.getQuantity() - step.getQuantity() * orderedMeal.getQuantity() < 0)
          return false;

        pos.setQuantity(pos.getQuantity() - step.getQuantity());
        /* temporarily update stock position */
      }
    }
    return true;
  }

  public static DbOrders beginNewOrderFromAPIOrder (JdbcTemplate jdbcTemplate, APIOrder apiOrder, DbUser dbUser)
  {
    /**
     * FIRST: Remove quants from stock,
     * THEN: Create order info
     * FINALLY: Remember ordered meals
     */

    if (!StockController.updateStockWithAPIOrder(jdbcTemplate, apiOrder))
      return null;

    DbOrders dbOrders = OrdersController.newDbOrdersFromAPIOrder(jdbcTemplate, apiOrder, dbUser);
    if (dbOrders == null)
      return null;

    if (!createDbOrderSetFromAPIOrder(jdbcTemplate, apiOrder, dbOrders))
      return null; /* WARN: At this point this should prob delete dbOrders instance */

    return dbOrders;
  }


  public static boolean createDbOrderSetFromAPIOrder (JdbcTemplate jdbcTemplate, APIOrder apiOrder, DbOrders dbOrders)
  {
    for (APIOrderedMeal apiOrderedMeal : apiOrder.getOrderedMeals())
    {
      DbOrder dbOrder = new DbOrder();
      dbOrder.setOrigin(dbOrders.getUuid());
      dbOrder.setMeal(apiOrderedMeal.getMealUUID());
      dbOrder.setQuantity(apiOrderedMeal.getQuantity());
      if (!insertOrder(jdbcTemplate, dbOrder))
        return false;
    }
    return true;
  }
}
