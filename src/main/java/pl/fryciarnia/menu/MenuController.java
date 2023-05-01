package pl.fryciarnia.menu;

import org.springframework.jdbc.core.JdbcTemplate;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.recipe.AdpRecipeIngridient;
import pl.fryciarnia.recipe.DbRecipe;
import pl.fryciarnia.recipe.RecipeController;
import pl.fryciarnia.recipe.RecipeMapping;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.stock.StockController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuController
{

  private static boolean isMealAvailable (HashMap<String, DbStock> stocks, List<DbRecipe> recipes)
  {
    for (DbRecipe ing : recipes)
    {
      if (!stocks.containsKey(ing.getIngridient()))
        return false;

      if (stocks.get(ing.getIngridient()).getQuantity() - ing.getQuantity() < 0)
        return false;
    }
    return true;
  }

  public static List<DbMeal> getAvailableMealsForHolding (JdbcTemplate jdbcTemplate, List<DbMeal> meals, DbHolding dbHolding)
  {
    /**
     * WARN: Optimization for DbStock.UUID lookups
     */
    HashMap<String, DbStock> fastStocks = new HashMap<>();
    List<DbStock> stocks = StockController.getStockByHolding(jdbcTemplate, dbHolding);

    if (stocks == null)
      return null; /* WARN: Serious issue */

    for (DbStock dbStock : stocks)
      fastStocks.put(dbStock.getIngridient(), dbStock);

    List<DbMeal> availableMeals = new ArrayList<>();
    for (DbMeal meal : meals)
    {
      List<DbRecipe> recipes = RecipeController.getRecipesByMeal(jdbcTemplate, meal);

      if (isMealAvailable(fastStocks, recipes))
        availableMeals.add(meal);
    }
    return availableMeals;
  }
}
