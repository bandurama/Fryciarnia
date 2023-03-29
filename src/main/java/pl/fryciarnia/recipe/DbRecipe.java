package pl.fryciarnia.recipe;

import lombok.Data;

/**
 * Jeden z korków przepisu na dany posiłek.
 * Oprucz instrukcji dla pracowników zawiera
 * informacje potrzebne dla systemu
 * zarządzania kuchnią, systemu sprzedaży
 * itp...
 */
@Data
public class DbRecipe
{
  private String meal;
  private String ingridient;
  private Float quantity;
  private String instruction;
  private Integer step;
}
