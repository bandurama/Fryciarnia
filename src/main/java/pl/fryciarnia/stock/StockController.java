package pl.fryciarnia.stock;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.ingridient.IngridientController;
import pl.fryciarnia.ingridient.IngridientMapping;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.order.APIOrder;
import pl.fryciarnia.order.APIOrderedMeal;
import pl.fryciarnia.recipe.DbRecipe;
import pl.fryciarnia.recipe.RecipeController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StockController
{
  public static boolean insertStock (JdbcTemplate jdbcTemplate, DbStock dbStock)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbStock VALUES(?, ?, ?, ?)",
              dbStock.getUuid(),
              dbStock.getHolding(),
              dbStock.getIngridient(),
              dbStock.getQuantity()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbStock> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
        (
            "SELECT * FROM DBSTOCK",
            BeanPropertyRowMapper.newInstance(DbStock.class)
        );
  }

  public static List<DbStock> getStockByHolding (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    return fetchAll(jdbcTemplate)
        .stream()
        .filter(dbStock -> dbStock.getHolding().equals(dbHolding.getUuid()))
        .toList();
  }

  public static DbStock getStockByUUID (JdbcTemplate jdbcTemplate, String uuid)
  {
    List<DbStock> dbStockList = fetchAll(jdbcTemplate);
    for (DbStock dbStock : dbStockList)
      if (dbStock.getUuid().equals(uuid))
        return dbStock;
    return null;
  }

  public static List<AdpStockIngridient> getAdpStockIngridientFromStockList (JdbcTemplate jdbcTemplate, List<DbStock> dbStockList)
  {
    List<AdpStockIngridient> adpStockIngridientList = new ArrayList<>();
    dbStockList.forEach(dbStock ->
    {
      DbIngridient dbIngridient = IngridientController.getIngridientByUUID(jdbcTemplate, dbStock.getIngridient());
      adpStockIngridientList.add(new AdpStockIngridient(dbStock, dbIngridient));
    });
    return adpStockIngridientList;
  }

  public static boolean updateStock (JdbcTemplate jdbcTemplate, DbStock dbStock)
  {
    try
    {
      jdbcTemplate.update
          (
              "UPDATE DBSTOCK SET HOLDING = ?, INGRIDIENT = ?, QUANTITY = ? WHERE UUID = ?",
              dbStock.getHolding(),
              dbStock.getIngridient(),
              dbStock.getQuantity(),
              dbStock.getUuid()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }


  public static boolean updateStockWithAPIOrder (JdbcTemplate jdbcTemplate, APIOrder apiOrder)
  {
    DbHolding dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, apiOrder.getHoldingUUID());
    if (dbHolding == null)
      return false;

    List<DbStock> dbStockList = StockController.getStockByHolding(jdbcTemplate, dbHolding);
    /* HACK: convert to HashMap for faster look up times */
    HashMap<String, DbStock> stock = new HashMap<>();
    for (DbStock dbStock : dbStockList)
      stock.put(dbStock.getIngridient(), dbStock);

    for (APIOrderedMeal apiOrderedMeal : apiOrder.getOrderedMeals())
    { /* for each meal update quantity in stock */
      DbMeal dbMeal = MealController.getMealByUUID(jdbcTemplate, apiOrderedMeal.getMealUUID());
      List<DbRecipe> dbRecipeList = RecipeController.getRecipesByMeal(jdbcTemplate, dbMeal);
      for (DbRecipe recipe : dbRecipeList)
      {
        if (!stock.containsKey(recipe.getIngridient()))
          return false;
        DbStock dbStock = stock.get(recipe.getIngridient());
        dbStock.setQuantity(dbStock.getQuantity() - recipe.getQuantity() * apiOrderedMeal.getQuantity());
        StockController.updateStock(jdbcTemplate, dbStock);
      }
    }
    return true;
  }

  public static boolean createStockListForExistingHolding (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    List<DbIngridient> dbIngridientList = IngridientController.fetchAll(jdbcTemplate);
    for (DbIngridient dbIngridient : dbIngridientList)
    {
      /* create stock entry for this ingridient in this particullar holding */
      DbStock dbStock = new DbStock();
      dbStock.setUuid(UUID.randomUUID().toString());
      dbStock.setIngridient(dbIngridient.getUuid());
      dbStock.setHolding(dbHolding.getUuid());
      dbStock.setQuantity(0.0f);
      if (!insertStock(jdbcTemplate, dbStock))
        return false;
    }
    return true;
  }
}
