package pl.fryciarnia.holding;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.user.DbUser;

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
}
