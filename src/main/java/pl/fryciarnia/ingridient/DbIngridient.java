package pl.fryciarnia.ingridient;

import lombok.Data;

/**
 * Pojedyńczy składnik przepisu, ogólny dla
 * każdego lokalu.
 */
@Data
public class DbIngridient
{
  private String uuid;
  private String name;
}
