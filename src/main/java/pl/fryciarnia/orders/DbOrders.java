package pl.fryciarnia.orders;

import lombok.Data;

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
}
