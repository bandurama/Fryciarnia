package pl.fryciarnia.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @SneakyThrows
    public static DbUser fromJSON (String json)
    {
        DbUser u = new DbUser();
        Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

        u.setUuid((String) m.get("uuid"));
        u.setIsGoogleAccount((Boolean) m.get("isGoogleAccount"));
        u.setMail((String) m.get("mail"));
        u.setName((String) m.get("name"));
        u.setSurname((String) m.get("surname"));
        u.setPassword((String) m.get("password"));
        u.setType(UserType.valueOf((String) m.get("type")));


        return u;
    }
}
