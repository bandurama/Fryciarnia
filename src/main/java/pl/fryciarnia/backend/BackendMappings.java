package pl.fryciarnia.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.fryciarnia.utils.APIDatagram;

import javax.sql.DataSource;

/**
 * Kontroler czysto prezentacyjny, do usunięcia w produkcji
 */
@RestController
public class BackendMappings
{
  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  private DataSource dataSource;

  @GetMapping("/reset")
  String BackendResetDatabase ()
  {
    APIDatagram apiDatagram = new APIDatagram();
    Resource resource = new ClassPathResource("init.sql");
    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);

    try
    {
      databasePopulator.execute(dataSource);
    }
    catch (Exception e)
    {
      return apiDatagram.fail(e.toString());
    }

    apiDatagram.setMsg("DB has been reseted");
    return apiDatagram.success();
  }
}
