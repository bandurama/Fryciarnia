package pl.fryciarnia.orders;

import lombok.Data;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.order.DbOrder;

@Data
public class AdpOrderMeal
{
  private DbMeal dbMeal;
  private DbOrder dbOrder;
}
