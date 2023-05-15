package pl.fryciarnia.kitchen;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.holding.HoldingMapping;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.orders.AdpOrdersAdpOrderMealDbHolding;
import pl.fryciarnia.orders.DbOrders;
import pl.fryciarnia.orders.OrdersController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;
import pl.fryciarnia.worker.DbWorker;
import pl.fryciarnia.worker.WorkerController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class KitchenMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/kitchen/orders")
  @ResponseBody
  public String APIKitchenOrders (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (dbUser == null || !dbUser.getType().equals(UserType.Kitchen))
      return apiDatagram.fail("PERMERR");

    DbWorker dbWorker = WorkerController.getWorkerByUser(jdbcTemplate, dbUser);
    if (dbWorker == null)
      return apiDatagram.fail("ISERR");

    DbHolding dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, dbWorker.getHolding());
    if (dbHolding == null)
      return apiDatagram.fail("ISERR");

    List<DbOrders> dbOrdersList = OrdersController.getOrdersByHolding(jdbcTemplate, dbHolding);
    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersAdpOrderMealDbHolding = OrdersController.getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, dbOrdersList);
    List<AdpDbUserAdpOrdersAdpOrderMealDbHolding> adpDbUserAdpOrdersAdpOrderMealDbHoldingList = new ArrayList<>();

    for (AdpOrdersAdpOrderMealDbHolding a : adpOrdersAdpOrderMealDbHolding)
    {
      if (!(a.getDbOrders().getOrderStatus().equals(OrderStatus.PAID) || a.getDbOrders().getOrderStatus().equals(OrderStatus.READY)))
        continue;
      DbUser u = UserController.getDbUserByUUID(jdbcTemplate, a.getDbOrders().getOwner());
      u.setPassword(""); /* securitas */
      adpDbUserAdpOrdersAdpOrderMealDbHoldingList.add(new AdpDbUserAdpOrdersAdpOrderMealDbHolding(u, a));
    }

    apiDatagram.setData(adpDbUserAdpOrdersAdpOrderMealDbHoldingList);
    return apiDatagram.success();
  }
}
