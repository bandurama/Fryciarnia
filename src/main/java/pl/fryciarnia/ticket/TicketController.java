package pl.fryciarnia.ticket;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.orders.DbOrders;
import pl.fryciarnia.orders.OrdersController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Idea of ticket is purely abstract
 * it exists only within real of order
 * as simple field.
 * Nonetheless, it's still very important
 * both for kitchen and for customer
 */
public class TicketController
{
  public static Integer getNextTicketByHoldingUUID (JdbcTemplate jdbcTemplate, DbHolding dbHolding)
  {
    List<DbOrders> dbOrdersList = OrdersController.getOrdersByHolding(jdbcTemplate, dbHolding);
    dbOrdersList = dbOrdersList
        .stream()
        .filter(order -> order.getOrderStatus().equals(OrderStatus.PAID))
        .toList();

    if (dbOrdersList.size() == 0)
      return 1;

    /* find any non-consecutive sequence in ticket listing */
    dbOrdersList = dbOrdersList
        .stream()
        .sorted(Comparator.comparing(DbOrders::getTicket))
        .collect(Collectors.toList());

    if (dbOrdersList.get(0).getTicket() != 1)
      return 1; /* no order starts with 1 */

    for (int i = 0; i < dbOrdersList.size() - 1; i++)
    {
      int currentTicket = dbOrdersList.get(i).getTicket() + 1;
      if (currentTicket != dbOrdersList.get(i + 1).getTicket())
        return currentTicket; /* broken sequence */
    }

    /* sequence not broken, so just que up another number */
    return dbOrdersList.get(dbOrdersList.size() - 1).getTicket() + 1;
  }
}
