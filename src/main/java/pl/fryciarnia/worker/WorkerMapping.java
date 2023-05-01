package pl.fryciarnia.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;

import java.util.List;
import java.util.Map;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class WorkerMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  /**
   * Can be accessed only by Holding manager, otherwise nothing will be returned
   * @param httpServletResponse
   * @param frySess
   * @return
   */
  @PostMapping("/api/worker/list")
  @ResponseBody
  public String APIDbWorkerList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (sessionUser == null || !sessionUser.getType().equals(UserType.Manager))
      return apiDatagram.fail("Session error");

    DbHolding dbHolding = HoldingController.getHoldingByManager(jdbcTemplate, sessionUser);
    if (dbHolding == null)
      return apiDatagram.fail("Manager has no holdings");

    List<AdpWorkerUser> holdingWorkers = WorkerController
      .fetchJoinedAll(jdbcTemplate)
      .stream()
      .filter(w -> w.getWorker().getHolding().equals(dbHolding.getUuid()))
      .toList();

    AdpWorkerSubtypes adpWorkerSubtypes = new AdpWorkerSubtypes();

    holdingWorkers.forEach(w -> {
      w.getUser().setPassword("");
      if (w.getWorker().getIsHardware())
        adpWorkerSubtypes.getHardware().add(w);
      else
        adpWorkerSubtypes.getPeople().add(w);
    });

    apiDatagram.setData(adpWorkerSubtypes);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/worker/cook/edit")
  @ResponseBody
  public String APIWorkerCookEdit (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);

    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Missing uuid key");

    String uuid = (String) m.get("uuid");

    /**
     * WARN: Might be editting different fileds
     *       it all depends on given keys
     */
    DbUser dbUser = UserController.getDbUserByUUID(jdbcTemplate, uuid);
    DbWorker dbWorker = WorkerController.getWorkerByUser(jdbcTemplate, dbUser);

    if (m.containsKey("name"))
    {
      dbUser.setName((String) m.get("name"));
      if (!UserController.updateUser(jdbcTemplate, dbUser))
        return apiDatagram.fail("Internal server error");
    }

    if (m.containsKey("surname"))
    {
      dbUser.setSurname((String) m.get("surname"));
      if (!UserController.updateUser(jdbcTemplate, dbUser))
        return apiDatagram.fail("Internal server error");
    }

    if (m.containsKey("salary"))
    {
      dbWorker.setSalary(Float.valueOf((String) m.get("salary")));
      if (!WorkerController.updateWorker(jdbcTemplate, dbWorker))
        return apiDatagram.fail("Internal server error");
    }

    apiDatagram.setData(dbWorker);
    return apiDatagram.success();
  }
  @PostMapping("/api/worker/hire")
  @ResponseBody
  public String APIDbWorkerHire (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (dbUser == null || !dbUser.getType().equals(UserType.Manager))
      return apiDatagram.fail("No perms");

    DbHolding dbHolding = HoldingController.getHoldingByManager(jdbcTemplate, dbUser);
    if (dbHolding == null)
      return apiDatagram.fail("Manager has no holdings");

    apiDatagram.setData(String.format("http://bandurama.ddns.net/register?holding=%s", dbHolding.getUuid()));
    return apiDatagram.success();
  }

}
