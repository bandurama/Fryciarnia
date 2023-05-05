package pl.fryciarnia.paypal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;

/**
 * Facade to decrypt paypal response
 */
@Data
public class FsdPaypalTransaction
{
  String id;
  String status;
  Float amount;

  public static FsdPaypalTransaction fromJSON (String json)
  {
    FsdPaypalTransaction fsdPaypalTransaction = new FsdPaypalTransaction();

    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(json).getAsJsonObject();

    fsdPaypalTransaction.setId(jsonObject.get("id").getAsString());
    fsdPaypalTransaction.setStatus(jsonObject.get("status").getAsString());

    String strAmount = jsonObject
        .getAsJsonArray("purchase_units")
        .get(0)
        .getAsJsonObject()
        .get("amount")
        .getAsJsonObject()
        .get("value")
        .getAsString();

    fsdPaypalTransaction.setAmount(Float.parseFloat(strAmount));
    return fsdPaypalTransaction;
  }
}
