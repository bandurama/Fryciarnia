package pl.fryciarnia.order;

import lombok.Data;

/**
 * Element pojedyńczego zamówenia - tzn. jeden
 * posiłek z całego zamówienia. Flaga isReady
 * dla ekranu wyświetlającego kolejkę.
 */
@Data
public class DbOrder
{
  private String origin;
  private String meal;
  private Boolean isReady;
}
