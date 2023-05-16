package pl.fryciarnia.kitchen;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.orders.DbOrders;
import pl.fryciarnia.orders.OrdersController;
import pl.fryciarnia.utils.APIDatagram;

public class KitchenController
{
  public static String perfOrdersStatusSet (JdbcTemplate jdbcTemplate, String uuid, OrderStatus orderStatus)
  {
    APIDatagram apiDatagram = new APIDatagram();
    DbOrders dbOrders = OrdersController.getOrdersByUUID(jdbcTemplate, uuid);
    if (dbOrders == null)
      return apiDatagram.fail("NOORDER");

    dbOrders.setOrderStatus(orderStatus);
    if (!OrdersController.updateOrders(jdbcTemplate, dbOrders))
      return apiDatagram.fail("SERVERR");

    return apiDatagram.success();
  }
}
