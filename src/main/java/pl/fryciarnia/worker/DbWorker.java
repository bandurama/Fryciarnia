package pl.fryciarnia.worker;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import pl.fryciarnia.user.UserType;

import java.util.Map;

/**
 * DbWorker to każdy pojedyńczy pracownik
 * dowiązany 1:1 do DbUser'a
 */
@Data
public class DbWorker
{
  private String uuid;
  private String worker;
  private String holding;
  private Float salary;
  private Boolean isHardware;

  @SneakyThrows
  public static DbWorker fromJSON (String json)
  {
    DbWorker w = new DbWorker();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

    w.setUuid((String) m.get("uuid"));
    w.setWorker((String) m.get("worker"));
    w.setHolding((String) m.get("holding"));
    w.setSalary(Float.valueOf((String) m.get("salary")));
    w.setIsHardware((Boolean) m.get("isHardware"));

    return w;
  }
}