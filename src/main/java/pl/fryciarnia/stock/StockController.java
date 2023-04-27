package pl.fryciarnia.stock;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.ingridient.IngridientController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.recipe.DbRecipe;

import java.util.ArrayList;
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
}
