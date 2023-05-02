package pl.fryciarnia.order;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;

import java.awt.*;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class OrderMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/order")
  @ResponseBody
  public String APIDbRecipeList(@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);

    /**
     * Make sure user is logged in either to their
     * web account or to holdin's terminal
     */
    if (dbUser == null)
      return apiDatagram.fail("NOLOGIN");

    if (!(dbUser.getType().equals(UserType.Web) || dbUser.getType().equals(UserType.Terminal)))
      return apiDatagram.fail("ACLVL");

    /**
     * Fetch order list and confirm possibility
     * of accepting it.
     */
    APIOrder apiOrder = APIOrder.fromJSON(body);

    if (!OrderController.canExecuteOrder(jdbcTemplate, apiOrder))
      return apiDatagram.fail("NOEXE");

    return apiDatagram.success();
  }
}
