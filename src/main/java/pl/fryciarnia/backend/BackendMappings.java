package pl.fryciarnia.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fryciarnia.utils.APIDatagram;

/**
 * Kontroler czysto prezentacyjny, do usunięcia w produkcji
 */
@RestController
public class BackendMappings
{
  @Autowired
  JdbcTemplate jdbcTemplate;

  @GetMapping("/reset")
  String BackendResetDatabase ()
  {
    APIDatagram apiDatagram = new APIDatagram();
    apiDatagram.setMsg("DB has been reseted");
    return apiDatagram.success();
  }
}
