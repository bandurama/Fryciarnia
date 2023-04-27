package pl.fryciarnia.worker;

import org.apache.catalina.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WorkerController
{

  public static boolean insertWorker (JdbcTemplate jdbcTemplate, DbWorker dbWorker)
  {
    try
    {
      jdbcTemplate.update
			(
					"INSERT INTO DBWORKER VALUES(?, ?, ?, ?, ?)",
					dbWorker.getUuid(),
					dbWorker.getWorker(),
					dbWorker.getHolding(),
					dbWorker.getSalary(),
          dbWorker.getIsHardware()
			);
    }
    catch (Exception e)
    {
      System.out.println(e);
      return false;
    }
    return true;
  }

  public static List<DbWorker> fetchAll (JdbcTemplate jdbcTemplate)
  {
    return jdbcTemplate.query
		(
				"SELECT * FROM DBWORKER",
				BeanPropertyRowMapper.newInstance(DbWorker.class)
		);
  }

  public static List<AdpWorkerUser> fetchJoinedAll (JdbcTemplate jdbcTemplate)
  {
    List<DbWorker> dbWorkerList = fetchAll(jdbcTemplate);
    List<AdpWorkerUser> adpWorkerUserList = new ArrayList<>();
    dbWorkerList.forEach(dbWorker ->
    {
      DbUser dbUser = UserController.getDbUserByUUID(jdbcTemplate, dbWorker.getWorker());
      adpWorkerUserList.add(new AdpWorkerUser(dbWorker, dbUser));
    });
    return adpWorkerUserList;
  }

  public static List<DbWorker> getWorkerByHolding (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    return fetchAll(jdbcTemplate)
        .stream()
        .filter(dbWorker -> dbWorker.getHolding().equals(dbHolding.getUuid()))
        .toList();
  }

  public static boolean newHardware (JdbcTemplate jdbcTemplate, DbHolding dbHolding, UserType userType)
  {
    if (!(userType.equals(UserType.Display) || userType.equals(UserType.Terminal)))
      return false;

    String nameCode = userType.equals(UserType.Display) ? "EKRAN" : "TERMINAL";
    String holdingCode = (String) Arrays.stream(dbHolding.getUuid().split("-")).toArray()[0];

    DbUser dbUser = new DbUser();
    dbUser.setUuid(UUID.randomUUID().toString());
    dbUser.setIsGoogleAccount(false);
    dbUser.setMail(nameCode + holdingCode);
    dbUser.setPassword(nameCode + holdingCode);
    dbUser.setName(nameCode);
    dbUser.setSurname("");
    dbUser.setType(userType);

    if (!UserController.insertUser(jdbcTemplate, dbUser))
      return false;

    DbWorker dbWorker = new DbWorker();
    dbWorker.setIsHardware(true);
    dbWorker.setUuid(UUID.randomUUID().toString());
    dbWorker.setWorker(dbUser.getUuid());
    dbWorker.setHolding(dbHolding.getUuid());
    dbWorker.setSalary(0f);
    WorkerController.insertWorker(jdbcTemplate, dbWorker);

    return true;
  }

  public static DbWorker getWorkerByUser (JdbcTemplate jdbcTemplate, DbUser dbUser)
  {
    List<DbWorker> dbWorkerList = fetchAll(jdbcTemplate);
    for (DbWorker dbWorker : dbWorkerList)
      if (dbWorker.getWorker().equals(dbUser.getUuid()))
        return dbWorker;
    return null;
  }

  public static boolean updateWorker (JdbcTemplate jdbcTemplate, DbWorker dbWorker)
  {
    try
    {
      jdbcTemplate.update
          (
              "UPDATE DBWORKER SET WORKER = ?, HOLDING = ?, SALARY = ?, ISHARDWARE = ? WHERE UUID = ?",
              dbWorker.getWorker(),
              dbWorker.getHolding(),
              dbWorker.getSalary(),
              dbWorker.getIsHardware(),
              dbWorker.getUuid()
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
