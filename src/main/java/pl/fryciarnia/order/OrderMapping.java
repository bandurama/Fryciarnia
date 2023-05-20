package pl.fryciarnia.order;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.orders.DbOrders;
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


  @PostMapping("/api/order/{isTakeout}")
  @ResponseBody
  public String APIDbOrdersOrder (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("isTakeout") String isTakeout)
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

    /**
     * All clear, prepare order and begin
     * awaiting the payment
     */
    DbOrders dbOrders = OrderController.beginNewOrderFromAPIOrder(jdbcTemplate, apiOrder, dbUser, isTakeout.equals("1"));
    if (dbOrders == null)
      return apiDatagram.fail("ERR");

    apiDatagram.setData(dbOrders);
    return apiDatagram.success();
  }
}
