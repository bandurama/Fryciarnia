package pl.fryciarnia.holding;

import lombok.Data;

/**
 * Reprezentacja pojedyńczego lokalu udostępnionego
 * we franczyzie.
 */
@Data
public class DbHolding
{
  private String uuid;
  private String localization;
  private String manager;
}
