package pl.fryciarnia.orders;

import lombok.Data;
import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import pl.fryciarnia.order.OrderStatus;

import java.util.Map;

import java.sql.Timestamp;

/**
 * Pojedyńcze zamówienie, składa się z wielu
 * produktów (DbOrder). Konkretne dla lokalu.
 */
@Data
public class DbOrders
{
  private String uuid;
  private Integer ticket;
  private Timestamp ctime;
  private String owner;
  private String holding;
  private OrderStatus orderStatus;
  private Boolean isTakeout;


  @SneakyThrows
  public static DbOrders fromJSON (String json)
  {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    return (gson.fromJson(json, DbOrders.class));
  }
}
