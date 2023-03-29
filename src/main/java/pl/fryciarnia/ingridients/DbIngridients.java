package pl.fryciarnia.ingridients;

import lombok.Data;

/**
 * Składnik przetrzymywany przez konkretny lokal,
 * system pilnuje zużytej ilości.
 */
@Data
public class DbIngridients
{
  private String holding;
  private String ingridient;
  private Float quantity;
}
