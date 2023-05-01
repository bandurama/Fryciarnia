package pl.fryciarnia.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.recipe.AdpRecipeIngridient;

import java.util.List;

@Data
public class AdpMealAdpRecipeIngridient
{
  private List<AdpRecipeIngridient> adpRecipeIngridient;
  private DbMeal dbMeal;
}
