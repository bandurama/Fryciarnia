package pl.fryciarnia.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.fryciarnia.ingridient.DbIngridient;

@AllArgsConstructor
@Data
public class AdpStockIngridient
{
  private DbStock dbStock;
  private DbIngridient dbIngridient;
}
