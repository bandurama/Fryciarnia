package pl.fryciarnia.backend;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping("/api/gauth")
  @ResponseBody
  public String APIGoogleAuth (@RequestBody String body, HttpServletResponse httpServletResponse)
  {
    System.out.println("WEIRD REQ: " + body);
    return "{\"ok\": true}";
  }
}
