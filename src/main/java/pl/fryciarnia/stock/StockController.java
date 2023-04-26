package pl.fryciarnia.stock;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;

import java.util.List;

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
            "SELECT * FROM DBINGRIDIENTS",
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
}
