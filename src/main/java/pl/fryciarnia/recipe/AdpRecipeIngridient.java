package pl.fryciarnia.recipe;

import lombok.Data;
import pl.fryciarnia.ingridient.DbIngridient;

/**
 * Adapter łączący krok przepisu (DbRecipe)
 * oraz składnik (DbIngridient) dla modułu
 * MealMapping oraz MealController
 */
@Data
public class AdpRecipeIngridient
{
  private DbRecipe dbRecipe;
  private DbIngridient dbIngridient;
}
