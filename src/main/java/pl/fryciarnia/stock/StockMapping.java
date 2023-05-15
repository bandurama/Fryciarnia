package pl.fryciarnia.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;
import pl.fryciarnia.worker.DbWorker;
import pl.fryciarnia.worker.WorkerController;

import java.util.List;
import java.util.Map;


@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class StockMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/stock/list")
  @ResponseBody
  public String APIDbStockList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    /**
     * WARN: Can only be called by manager or kitchen user
     */
    APIDatagram apiDatagram = new APIDatagram();

    DbUser sessionUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);

    if (sessionUser == null || (!sessionUser.getType().equals(UserType.Manager)
                             && !sessionUser.getType().equals(UserType.Kitchen)))
      return apiDatagram.fail("Session error");

    DbHolding dbHolding = null;

    if (sessionUser.getType().equals(UserType.Manager))
      dbHolding = HoldingController.getHoldingByManager(jdbcTemplate, sessionUser);
    else if (sessionUser.getType().equals(UserType.Kitchen))
    {
      DbWorker dbWorker = WorkerController.getWorkerByUser(jdbcTemplate, sessionUser);
      dbHolding = HoldingController.getHoldingByUUID(jdbcTemplate, dbWorker.getHolding());
    }
    else
      return apiDatagram.fail("Internal server error");

    if (dbHolding == null)
      return apiDatagram.fail("Unable to fetch holding by this user");

    List<DbStock> dbStockList = StockController.getStockByHolding(jdbcTemplate, dbHolding);
    apiDatagram.setData(StockController.getAdpStockIngridientFromStockList(jdbcTemplate, dbStockList));
    return apiDatagram.success();
  }


  @SneakyThrows
  @PostMapping("/api/stock/quantity/edit")
  @ResponseBody
  public String APIStockQuantityEdit (@RequestBody String body, HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();
    Map<String, Object> m = (new ObjectMapper()).readValue(body, Map.class);

    if (!(m.containsKey("uuid") && m.containsKey("quantity")))
      return apiDatagram.fail("Missing keys");

      String stockUUID = (String) m.get("uuid");
      Float stockQuantity = Float.valueOf((String) m.get("quantity"));

      DbStock newStock = StockController.getStockByUUID (jdbcTemplate, stockUUID);
      newStock.setQuantity(stockQuantity);
      if (!StockController.updateStock(jdbcTemplate, newStock))
        return apiDatagram.fail("Internal server error");

      apiDatagram.setData(newStock);
      return apiDatagram.success();
  }
}
