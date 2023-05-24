package pl.fryciarnia.holding;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.session.DbSession;
import pl.fryciarnia.session.SessionController;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.stock.StockController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;
import pl.fryciarnia.worker.WorkerController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class HoldingMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/holding/list")
  @ResponseBody
  public String APIDbHoldingList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    List<DbHolding> holdings = HoldingController.fetchAll(jdbcTemplate);

    holdings.forEach(holding ->
    {
      DbUser manager = UserController.getDbUserByUUID(jdbcTemplate, holding.getManager());
      if (manager != null)
				holding.setManager
				(
						String.format("%s %s",
							manager.getName(),
							manager.getSurname())
				);
    });
    apiDatagram.setData(holdings);
    return apiDatagram.success();
  }

  @PostMapping("/api/holding/managers/{showAll}")
  @ResponseBody
  public String APIDbHoldingManagers (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("showAll") Integer showAll)
  {
    APIDatagram apiDatagram = new APIDatagram();
    List<DbUser> allUsers = UserController.fetchAll(jdbcTemplate);
    List<DbHolding> dbHoldings = HoldingController.fetchAll(jdbcTemplate);

    /**
     * HOTFIX: Make this smarter
     */
    if (showAll != 0)
    {
      apiDatagram.setData(allUsers
          .stream()
          .filter(user -> user.getType() == UserType.Manager && !dbHoldings.stream().anyMatch(h -> h.getManager().equals(user.getUuid())))
          .toList());
    }
    else
    {
      apiDatagram.setData(allUsers
          .stream()
          .filter(user -> user.getType() == UserType.Manager)
          .toList());
    }

    return apiDatagram.success();
  }


  @SneakyThrows
  @PostMapping("/api/holding/info")
  @ResponseBody
  public String APIDbHoldingInfo (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");
    DbHolding dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, uuid);

    if (dbHolding == null)
      return apiDatagram.fail("Holding doesn't exists");

    apiDatagram.setData(dbHolding);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/holding/remove")
  @ResponseBody
  public String APIDbHoldingRemove (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");

    if (frySess.equals("nil"))
      return apiDatagram.fail("No session");

    DbUser user = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (!user.getType().equals(UserType.Admin))
      return apiDatagram.fail("No perms");

    DbHolding holdingToRemove = HoldingController.getHoldingByUUID(jdbcTemplate, uuid);

    if (holdingToRemove == null)
      return apiDatagram.fail("No holding");

    if (!HoldingController.removeHolding(jdbcTemplate, holdingToRemove))
      return apiDatagram.fail("Internal server err");

    return apiDatagram.success();
  }

  @PostMapping("/api/holding/edit")
  @ResponseBody
  public String APIDbHoldingEdit (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    /* Confirm session blongs to admin */
    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (sessionUser == null || !sessionUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("Invalid session");

    /* update data */
    DbHolding dbHolding = DbHolding.fromJSON(body);

    /**
     * WARN: Confirm that this particular manager
     *       doesn't heave any more holdings
     *       assigned to 'em
     */
    List<DbHolding> dbHoldingList = HoldingController.fetchAll(jdbcTemplate);
    if (dbHoldingList.stream().anyMatch(holding -> holding.getManager().equals(dbHolding.getManager()) && !holding.getUuid().equals(dbHolding.getUuid())))
      return apiDatagram.fail("Nie można być managerem wielu lokacji jednocześnie!");

    if (!HoldingController.updateHolding(jdbcTemplate, dbHolding))
      return apiDatagram.fail("Internal server error");

    return apiDatagram.success();
  }

  @PostMapping("/api/holding/insert")
  @ResponseBody
  public String APIDbHoldingInsert (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    /* Confirm session blongs to admin */
    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (sessionUser == null || !sessionUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("Invalid session");

    /* insert data */
    DbHolding dbHolding = DbHolding.fromJSON(body);
    dbHolding.setUuid(UUID.randomUUID().toString());

    if (dbHolding.getManager() == null || dbHolding.getManager().equals(""))
      return apiDatagram.fail("Nie podano managera");

    DbUser manager = UserController.getDbUserByUUID(jdbcTemplate, dbHolding.getManager());
    if (manager == null)
      return apiDatagram.fail("Podany manager nie istnieje");

    if (dbHolding.getLocalization() == null || dbHolding.getLocalization().equals(""))
      return apiDatagram.fail("Nie podano nazwy");

    if (!HoldingController.insertHolding(jdbcTemplate, dbHolding))
      return apiDatagram.fail("Internal server error");

    /* create hardware accounts */
    boolean errorLevel = true;
    errorLevel &= WorkerController.newHardware(jdbcTemplate, dbHolding, UserType.Display);
    errorLevel &= WorkerController.newHardware(jdbcTemplate, dbHolding, UserType.Terminal);

    if (!errorLevel)
      return apiDatagram.fail("Internal server error regarding workers registration");

    /* assing all ingridients to the holding's kitchen */
    if (!StockController.createStockListForExistingHolding(jdbcTemplate, dbHolding))
      return apiDatagram.fail("Failed to create new stock list");

    apiDatagram.setData(dbHolding);
    return apiDatagram.success();
  }
}
