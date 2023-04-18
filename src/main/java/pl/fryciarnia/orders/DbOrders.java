package pl.fryciarnia.orders;

import lombok.Data;
import lombok.SneakyThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
  private Boolean isCasched;
  private Boolean isOut;
  private Boolean isTakeout;
  private Boolean  isCancelled;
  private Boolean isReady;

  @SneakyThrows
  public static DbOrders fromJSON (String json)
  {
    /**
     * WARN: Casts to Timestamp AND Boolean might NOT work!
     */
    DbOrders self = new DbOrders();
    Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);
    self.setUuid((String) m.get("uuid"));
    self.setHolding((String) m.get("holding"));
    self.setOwner((String) m.get("owner"));
    self.setCtime(Timestamp.valueOf((String) m.get("ctime")));
    self.setIsOut((Boolean) m.get("isOut"));
    self.setIsCasched((Boolean) m.get("isCasched"));
    self.setIsCancelled((Boolean) m.get("isCancelled"));
    self.setIsReady((Boolean) m.get("isReady"));
    self.setIsTakeout((Boolean) m.get("isTakeout"));
		self.setTicket((Integer) m.get("ticket"));

    return self;
  }
}
