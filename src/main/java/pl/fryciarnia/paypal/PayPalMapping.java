package pl.fryciarnia.paypal;

import com.braintreegateway.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.order.DbOrder;
import pl.fryciarnia.order.OrderController;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.orders.DbOrders;
import pl.fryciarnia.orders.OrdersController;
import pl.fryciarnia.utils.APIDatagram;

import java.math.BigDecimal;
import java.util.Map;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class PayPalMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private PayPalService payPalService;

  public static final float __max_diff_tolerance = 0.1F;

  @SneakyThrows
  @PostMapping("/api/paypal/{id}/{order}")
  @ResponseBody
  public String APIDbPayPal (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("id") String id, @PathVariable("order") String order)
  {
    APIDatagram apiDatagram = new APIDatagram();
    DbOrders dbOrders = OrdersController.getOrdersByUUID(jdbcTemplate, order);
    if (dbOrders == null)
      return apiDatagram.fail("ISE");

    String trDetailsJSON = payPalService.getTransactionDetails(id);
    FsdPaypalTransaction fsdPaypalTransaction
        = FsdPaypalTransaction.fromJSON(trDetailsJSON);

    if (!fsdPaypalTransaction.getStatus().equals("COMPLETED"))
      return apiDatagram.fail("NOPAY");

    /* Confirm price is right */
    float price =  OrdersController.getPriceByDbOrders(jdbcTemplate, dbOrders);

    /**
     * WARN: Sharp float compilations aren't great ideas
     *       so we check the difference with certain tolerance;
     */

    float priceDiff = Math.abs(price - fsdPaypalTransaction.getAmount());

    if (priceDiff > __max_diff_tolerance)
      return apiDatagram.fail("NOPAY");

    /* mark order as PAID */
    dbOrders.setOrderStatus(OrderStatus.PAID);
    if (!OrdersController.updateOrders(jdbcTemplate, dbOrders))
      return apiDatagram.fail("SERVERR");

    return apiDatagram.success();
  }

}
