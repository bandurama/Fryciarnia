package pl.fryciarnia.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Data;
import lombok.SneakyThrows;
import java.util.Map;

/**
 * WebUser to każdy pojedyńczy użytkownik zarejestrowany
 * na stronie serwisu.
 */
@Data
public class DbUser
{
    private String uuid;
    private Boolean isGoogleAccount;
    private String mail;
    private String name;
    private String surname;
    private String password;
    private UserType type;

    public static DbUser fromJSON (String json)
    {
        try
        {
            return (new Gson()).fromJson(json, DbUser.class);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
