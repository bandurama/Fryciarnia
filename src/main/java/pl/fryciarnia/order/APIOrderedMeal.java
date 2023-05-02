package pl.fryciarnia.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIOrderedMeal
{
  private String mealUUID;
  private Integer quantity;
}
