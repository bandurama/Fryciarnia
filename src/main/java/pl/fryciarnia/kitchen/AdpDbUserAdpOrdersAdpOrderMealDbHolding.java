package pl.fryciarnia.kitchen;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.fryciarnia.orders.AdpOrdersAdpOrderMealDbHolding;
import pl.fryciarnia.user.DbUser;

@Data
@AllArgsConstructor
public class AdpDbUserAdpOrdersAdpOrderMealDbHolding
{
  DbUser dbUser;
  AdpOrdersAdpOrderMealDbHolding adpOrdersAdpOrderMealDbHolding;
}
