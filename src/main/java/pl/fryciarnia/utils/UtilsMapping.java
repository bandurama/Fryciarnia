package pl.fryciarnia.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;

import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class UtilsMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;


  @GetMapping("/red/locations")
  public RedirectView RedLocations (@CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    RedirectView redirectView = new RedirectView();
    List<DbHolding> dbHoldingList = HoldingController.fetchAll(jdbcTemplate);
    StringBuilder stringBuilder = new StringBuilder("https://www.google.com/maps/dir");

    for (DbHolding dbHolding : dbHoldingList)
      stringBuilder.append(String.format("/%s", dbHolding.getLocalization()));

    redirectView.setUrl(stringBuilder.toString());
    return redirectView;
  }
}
