package pl.fryciarnia.holding;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;

import java.util.List;
import java.util.UUID;

public class HoldingController
{
  public static boolean insertHolding (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    try
    {
      jdbcTemplate.update
          (
              "INSERT INTO DbHolding VALUES(?, ?, ?)",
              dbHolding.getUuid(),
              dbHolding.getLocalization(),
              dbHolding.getManager()
          );
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }

    return true;
  }

	public static List<DbHolding> fetchAll (JdbcTemplate jdbcTemplate)
	{
			return jdbcTemplate.query
			(
				"SELECT * FROM DBHOLDING",
				BeanPropertyRowMapper.newInstance(DbHolding.class)
			);
	}

  public static DbHolding getHoldingByUUID (JdbcTemplate jdbcTemplate, String uuid)
  {
    List<DbHolding> holdings = fetchAll(jdbcTemplate);
    for (DbHolding h : holdings)
      if (h.getUuid().equals(uuid))
        return h;
    return null;
  }

  public static DbHolding getHoldingByManager (JdbcTemplate jdbcTemplate, DbUser manager)
  {
    List<DbHolding> holdings = fetchAll(jdbcTemplate);
    for (DbHolding h : holdings)
      if (h.getManager().equals(manager.getUuid()))
        return h;
    return null;
  }

  public static boolean removeHolding (JdbcTemplate jdbcTemplate, DbHolding holding)
  {
    try
    {
      jdbcTemplate.update ("DELETE FROM DBORDERS WHERE HOLDING = ?", new Object [] { holding.getUuid() });
      jdbcTemplate.update ("DELETE FROM DBSTOCK WHERE HOLDING = ?", new Object [] { holding.getUuid() });
      jdbcTemplate.update ("DELETE FROM DBHOLDING WHERE UUID = ?", new Object [] { holding.getUuid() });
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static boolean updateHolding (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    try
    {
      jdbcTemplate.update
          (
              "UPDATE DBHOLDING SET LOCALIZATION = ?, MANAGER = ? WHERE UUID = ?",
              dbHolding.getLocalization(),
              dbHolding.getManager(),
              dbHolding.getUuid()
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
