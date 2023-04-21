package pl.fryciarnia.utils;

import com.google.gson.Gson;
import lombok.Data;

/**
 * Uniwersalny adapter do enkapsulacji wyższych
 * obiektów, przesyłanych z API do klienta w
 * postaci JSON'a
 */
@Data
public class APIDatagram
{
  private Object data;  /* enkapsuowany obiekt odsyłany */
  private boolean ok;   /* informacja o poprawności żądania */
  private String msg;   /* dodatkowy komunikat informacyjny */

  @Override
  public String toString()
  {
    return (new Gson()).toJson(this);
  }

  public String fail (String cause)
  {
    setOk(false);
    setMsg(cause);
    return toString();
  }

  public String success ()
  {
    setOk(true);
    return toString();
  }
}
