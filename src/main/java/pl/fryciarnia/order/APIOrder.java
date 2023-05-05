package pl.fryciarnia.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.*;

/**
 * This is a packet sent to REST end point from client
 * that describes entire order => that is DbOrders with [dbOrder...]
 */
@Data
public class APIOrder
{
  private String holdingUUID;
  private String orderUUID;
  private List<APIOrderedMeal> orderedMeals;

  @SneakyThrows
  public static APIOrder fromJSON (String json)
  {
    return (new Gson()).fromJson(json, APIOrder.class);
  }
}
