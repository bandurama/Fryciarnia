package pl.fryciarnia.ingridient;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.catalina.User;
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
import java.util.UUID;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class IngridientMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/ingridient/list")
  @ResponseBody
  public String APIDbIngridientList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    List<DbIngridient> ingridientList = IngridientController.fetchAll(jdbcTemplate);
    apiDatagram.setData(ingridientList);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/ingridient/info")
  @ResponseBody
  public String APIDbIngridientInfo (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);
    if (!m.containsKey("uuid"))
      return apiDatagram.fail("Requires uuid to proceed");

    String uuid = (String) m.get("uuid");
    DbIngridient dbIngridient = IngridientController.getIngridientByUUID(jdbcTemplate, uuid);

    if (dbIngridient == null)
      return apiDatagram.fail("Ingridient does not exists");

    apiDatagram.setData(dbIngridient);
    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/ingridient/insert")
  @ResponseBody
  public String APIDbIngridientInsert (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    DbIngridient dbIngridient = DbIngridient.fromJSON(body);
    dbIngridient.setUuid(UUID.randomUUID().toString());

    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (sessionUser == null || !sessionUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("Session error");

    if (!IngridientController.insertIngridient(jdbcTemplate, dbIngridient))
      return apiDatagram.fail("Internal Server ErrorPage");

    apiDatagram.setData(dbIngridient);
    return apiDatagram.success();
  }

  @PostMapping("/api/ingridient/edit")
  @ResponseBody
  public String APIDbIngridientEdit (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    /* Confirm session blongs to admin */
    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (sessionUser == null || !sessionUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("Invalid session");

    /* update data */
    DbIngridient dbIngridient = DbIngridient.fromJSON(body);
    if (!IngridientController.updateIngridient(jdbcTemplate, dbIngridient))
      return apiDatagram.fail("Internal server error");

    return apiDatagram.success();
  }

  @SneakyThrows
  @PostMapping("/api/ingridient/remove")
  @ResponseBody
  public String APIDbIngridientRemove (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
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

    DbIngridient ingridientToRemove = IngridientController.getIngridientByUUID(jdbcTemplate, uuid);

    if (ingridientToRemove == null)
      return apiDatagram.fail("No ingridient");

    if (!IngridientController.removeIngridient(jdbcTemplate, ingridientToRemove))
      return apiDatagram.fail("Internal server err");

    return apiDatagram.success();
  }
}
