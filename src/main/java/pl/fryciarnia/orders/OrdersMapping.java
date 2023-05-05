package pl.fryciarnia.orders;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.order.DbOrder;
import pl.fryciarnia.order.OrderController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class OrdersMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/orders/info/{uuid}")
  @ResponseBody
  public String APIDbOrdersList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("uuid") String uuid)
  {
    APIDatagram apiDatagram = new APIDatagram();
    ArrayList<DbOrders> myDbOrderInList = new ArrayList<>();
    myDbOrderInList.add(OrdersController.getOrdersByUUID(jdbcTemplate, uuid));
    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersAdpOrderMealDbHoldings = OrdersController.getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, myDbOrderInList);
    apiDatagram.setData(adpOrdersAdpOrderMealDbHoldings.get(0));
    return apiDatagram.success();
  }

  @PostMapping("/api/orders/list")
  @ResponseBody
  public String APIDbOrdersList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (dbUser == null)
      return apiDatagram.fail("NO SESS");

    List<DbOrders> ordersList = OrdersController.fetchAll(jdbcTemplate);

    /* Filter regarding user type */
    if (dbUser.getType().equals(UserType.Web))
      ordersList = ordersList.stream().filter(order -> order.getOwner().equals(dbUser.getUuid())).toList();
    else if (dbUser.getType().equals(UserType.Manager))
    {
      DbHolding dbHolding = HoldingController.getHoldingByManager(jdbcTemplate, dbUser);
      if (dbHolding == null)
        return apiDatagram.fail("Manager has no holding");
      ordersList = ordersList.stream().filter(order -> order.getHolding().equals(dbHolding.getUuid())).toList();
    }
    else if (!dbUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("NO PERMS");

    /**
     * orderList contains only valid positions,
     * now pack 'em into adapter
     */
    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersMeals = OrdersController.getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, ordersList);
    apiDatagram.setData(adpOrdersMeals);
    return apiDatagram.success();
  }


}
