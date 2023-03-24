package pl.fryciarnia.webuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

/**
 * WebUser to każdy pojedyńczy użytkownik zarejestrowany
 * na stronie serwisu.
 */
@Data
public class WebUser
{
    private String uuid;
    private Boolean isGoogleAccount;
    private String mail;
    private String password;
    private WebUserType type;

    private Boolean success;
    private String msg;

    @SneakyThrows
    public static WebUser fromJSON (String json)
    {
        WebUser u = new WebUser();
        Map<String, Object> m = (new ObjectMapper()).readValue(json, Map.class);

        u.setUuid((String) m.get("uuid"));
        u.setIsGoogleAccount((Boolean) m.get("isGoogleAccount"));
        u.setMail((String) m.get("mail"));
        u.setPassword((String) m.get("password"));
        u.setType(WebUserType.valueOf((String) m.get("type")));

        return u;
    }
}