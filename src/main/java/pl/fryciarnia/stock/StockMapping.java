package pl.fryciarnia.stock;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;


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

    return apiDatagram.success();
  }

}
