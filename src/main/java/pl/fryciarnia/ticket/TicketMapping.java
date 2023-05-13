package pl.fryciarnia.ticket;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.orders.DbOrders;
import pl.fryciarnia.orders.OrdersController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;
import pl.fryciarnia.worker.DbWorker;
import pl.fryciarnia.worker.WorkerController;

import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class TicketMapping
{
  @Autowired
  JdbcTemplate jdbcTemplate;


  @PostMapping("/api/tickets")
  @ResponseBody
  public String APIDbPayPal (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (dbUser == null || !dbUser.getType().equals(UserType.Display))
      return apiDatagram.fail("ACLV");

    DbWorker dbWorker = WorkerController.getWorkerByUser(jdbcTemplate, dbUser);
    if (dbWorker == null)
      return apiDatagram.fail("SRVERR");

    DbHolding dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, dbWorker.getHolding());
    if (dbHolding == null)
      return apiDatagram.fail("SRVERR");

    List<DbOrders> dbOrdersList = OrdersController.getOrdersByHolding(jdbcTemplate, dbHolding)
        .stream()
        .filter(o -> o.getOrderStatus().equals(OrderStatus.PAID) || o.getOrderStatus().equals(OrderStatus.READY))
        .toList();

    apiDatagram.setData(dbOrdersList);
    return apiDatagram.success();
  }
}
