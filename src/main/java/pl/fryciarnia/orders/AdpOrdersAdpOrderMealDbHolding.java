package pl.fryciarnia.orders;

import lombok.Data;
import pl.fryciarnia.holding.DbHolding;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdpOrdersAdpOrderMealDbHolding
{
  private DbOrders dbOrders;
  private List<AdpOrderMeal> adpOrderMeals;
  private DbHolding dbHolding;
  
  public AdpOrdersAdpOrderMealDbHolding()
  {
    this.adpOrderMeals = new ArrayList<>();
  }
}
