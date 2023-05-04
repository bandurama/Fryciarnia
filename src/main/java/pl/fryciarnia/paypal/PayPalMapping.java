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
    Map<String, Object> m = (new ObjectMapper()).readValue(trDetailsJSON, Map.class);

    if (!m.containsKey("status") || !m.containsKey("id"))
      return apiDatagram.fail("SERV");

    if (!m.get("id").toString().equals(dbOrders.getUuid()))
      return apiDatagram.fail("SEC");

    if (!m.get("status").equals("COMPLETED"))
      return apiDatagram.fail("NOPAY");

    return apiDatagram.success();
  }

}
