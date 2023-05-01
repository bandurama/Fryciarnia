package pl.fryciarnia.ingridient;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.stock.StockController;

import java.util.List;
import java.util.UUID;

public class IngridientController
{
  public static boolean insertIngridient (JdbcTemplate jdbcTemplate, DbIngridient dbIngridient)
  {
    /**
     * WARN: Each instance of holding has its own DbStock
     *       that 1:1 mirrors stocking quantities of
     *       current ingridient!
     */
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbIngridient VALUES(?, ?, ?)",
              dbIngridient.getUuid(),
              dbIngridient.getName(),
              dbIngridient.getIcon()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }

    /* NOW: for each instance of DbHolding create repr of stock item */
    List<DbHolding> dbHoldingList = HoldingController.fetchAll(jdbcTemplate);
    DbStock bluePrint = new DbStock();
    bluePrint.setIngridient(dbIngridient.getUuid());
    bluePrint.setQuantity(0.0f);

    for (DbHolding dbHolding : dbHoldingList)
    {
      bluePrint.setHolding(dbHolding.getUuid());
      bluePrint.setUuid(UUID.randomUUID().toString());
      if (!StockController.insertStock(jdbcTemplate, bluePrint))
        return false; /* THIS CANNOT HAPPEN */
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
      jdbcTemplate.update ("DELETE FROM DBSTOCK WHERE INGRIDIENT = ?", new Object [] { ingridient.getUuid() });
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
              "UPDATE DBINGRIDIENT SET NAME = ?, ICON = ? WHERE UUID = ?",
              dbIngridient.getName(),
              dbIngridient.getIcon(),
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
