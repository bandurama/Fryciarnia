package pl.fryciarnia.orders;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pl.fryciarnia.holding.DbHolding;
import pl.fryciarnia.holding.HoldingController;
import pl.fryciarnia.ingridient.DbIngridient;
import pl.fryciarnia.ingridient.IngridientController;
import pl.fryciarnia.kitchen.AdpDbUserAdpOrdersAdpOrderMealDbHolding;
import pl.fryciarnia.kitchen.KitchenController;
import pl.fryciarnia.meal.DbMeal;
import pl.fryciarnia.meal.MealController;
import pl.fryciarnia.order.DbOrder;
import pl.fryciarnia.order.OrderController;
import pl.fryciarnia.order.OrderStatus;
import pl.fryciarnia.recipe.AdpRecipeIngridient;
import pl.fryciarnia.recipe.DbRecipe;
import pl.fryciarnia.recipe.RecipeController;
import pl.fryciarnia.stock.DbStock;
import pl.fryciarnia.stock.StockController;
import pl.fryciarnia.user.DbUser;
import pl.fryciarnia.user.UserController;
import pl.fryciarnia.user.UserType;
import pl.fryciarnia.utils.APIDatagram;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(allowCredentials = "true", origins = "http://bandurama.ddns.net")
@RestController
public class OrdersMapping
{
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @PostMapping("/api/orders/info/{uuid}")
  @ResponseBody
  public String APIDbOrdersList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("uuid") String uuid)
  {
    APIDatagram apiDatagram = new APIDatagram();
    ArrayList<DbOrders> myDbOrderInList = new ArrayList<>();
    myDbOrderInList.add(OrdersController.getOrdersByUUID(jdbcTemplate, uuid));
    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersAdpOrderMealDbHoldings = OrdersController.getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, myDbOrderInList);
    apiDatagram.setData(adpOrdersAdpOrderMealDbHoldings.get(0));
    return apiDatagram.success();
  }

  @PostMapping("/api/orders/list")
  @ResponseBody
  public String APIDbOrdersList (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbUser dbUser = UserController.getDbUserBySessionToken(jdbcTemplate, frySess);
    if (dbUser == null)
      return apiDatagram.fail("NO SESS");

    List<DbOrders> ordersList = OrdersController.fetchAll(jdbcTemplate);

    /* Filter regarding user type */
    if (dbUser.getType().equals(UserType.Web))
      ordersList = ordersList.stream().filter(order -> order.getOwner().equals(dbUser.getUuid())).toList();
    else if (dbUser.getType().equals(UserType.Manager))
    {
      DbHolding dbHolding = HoldingController.getHoldingByManager(jdbcTemplate, dbUser);
      if (dbHolding == null)
        return apiDatagram.fail("Manager has no holding");
      ordersList = ordersList.stream().filter(order -> order.getHolding().equals(dbHolding.getUuid())).toList();
    }
    else if (!dbUser.getType().equals(UserType.Admin))
      return apiDatagram.fail("NO PERMS");

    /**
     * orderList contains only valid positions,
     * now pack 'em into adapter
     */
    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersMeals = OrdersController.getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, ordersList);
    apiDatagram.setData(adpOrdersMeals);
    return apiDatagram.success();
  }

  @PostMapping("/api/orders/paid/{uuid}")
  @ResponseBody
  public String APIKitchenTaken (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("uuid") String uuid)
  {
    return KitchenController.perfOrdersStatusSet(jdbcTemplate, uuid, OrderStatus.PAID);
  }

  @PostMapping("/api/orders/fail/{uuid}")
  @ResponseBody
  public String APIOrdersFail (HttpServletResponse httpServletResponse, @CookieValue(value = "fry_sess", defaultValue = "nil") String frySess, @PathVariable("uuid") String uuid)
  {
    APIDatagram apiDatagram = new APIDatagram();

    DbOrders dbOrders = OrdersController.getOrdersByUUID(jdbcTemplate, uuid);
    if (dbOrders == null)
      return apiDatagram.fail("SRVERR");

    List<DbOrders> dbOrdersList = new ArrayList<>();
    dbOrdersList.add(dbOrders);

    List<AdpOrdersAdpOrderMealDbHolding> adpOrdersAdpOrderMealDbHoldingList
        = OrdersController.getAdpOrdersAdpOrderMealDbHolding(jdbcTemplate, dbOrdersList);
    AdpOrdersAdpOrderMealDbHolding adpOrdersAdpOrderMealDbHolding
        = adpOrdersAdpOrderMealDbHoldingList.get(0);

    /**
     * IN FACT: List from abolve has to have only one single element
     *          therefore .get(0) is correct.
     */
    List<DbStock> dbStockList = StockController.getStockByHolding(jdbcTemplate, adpOrdersAdpOrderMealDbHolding.getDbHolding());
    HashMap<String, DbStock> fastStock = new HashMap<>();

    /* { ingridientUUID -> stockItem } */
    for (DbStock dbStock : dbStockList)
      fastStock.put(dbStock.getIngridient(), dbStock);

    for (AdpOrderMeal adpOrderMeal : adpOrdersAdpOrderMealDbHolding.getAdpOrderMeals())
    {
      List<DbRecipe> dbRecipeList = RecipeController.getRecipesByMeal(jdbcTemplate, adpOrderMeal.getDbMeal());
      for (DbRecipe dbRecipe : dbRecipeList)
      { /* get stock from current holding for this ingridient */
        if (!fastStock.containsKey(dbRecipe.getIngridient()))
          continue; /* this should not happen, but if does, its not big of a deal */
        DbStock dbStock = fastStock.get(dbRecipe.getIngridient());
        float calculatedQuantity = dbRecipe.getQuantity() * adpOrderMeal.getDbOrder().getQuantity();
        dbStock.setQuantity(dbStock.getQuantity() + calculatedQuantity);
      }
    }

    /* update database */
    for (String k : fastStock.keySet())
      StockController.updateStock(jdbcTemplate, fastStock.get(k));

    return KitchenController.perfOrdersStatusSet(jdbcTemplate, uuid, OrderStatus.FAILED);
  }
}
