package pl.fryciarnia.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class Test
{

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @GetMapping("/test")
  public String mappingTest ()
  {
    return "{\"ok\": true}";
  }
}
