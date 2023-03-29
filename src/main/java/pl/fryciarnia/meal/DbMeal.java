package pl.fryciarnia.meal;

import lombok.Data;

/**
 * Posiłek / Danie / Oferta ogólna dla
 * wszystkich lokali.
 */
@Data
public class DbMeal
{
  private String uuid;
  private String name;
  private float price;
}
