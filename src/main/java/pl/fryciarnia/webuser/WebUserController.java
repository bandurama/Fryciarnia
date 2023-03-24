package pl.fryciarnia.webuser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class WebUserController
{
    public static List<WebUser> fetchAll (JdbcTemplate jdbcTemplate)
    {
        return jdbcTemplate.query
        (
					"SELECT * FROM WEBUSER",
					BeanPropertyRowMapper.newInstance(WebUser.class)
        );
    }

    public static WebUser getWebUserByUUID (JdbcTemplate jdbcTemplate, String uuid)
    {
        List<WebUser> all = WebUserController.fetchAll(jdbcTemplate);
        for (WebUser user : all)
            if (user.getUuid().equals(uuid))
                return user;

        return null;
    }


    public static boolean updateUser (JdbcTemplate jdbcTemplate, WebUser webUser)
    {
        try
        {
            jdbcTemplate.update
            (
							"UPDATE WebUser SET isGoogleAccount = ?, mail = ?, password = ?, type = ? WHERE uuid = ?",
							webUser.getIsGoogleAccount(),
							webUser.getMail(),
							webUser.getPassword(),
							webUser.getType(),
							webUser.getUuid()
            );
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
        return true;
    }

		public static boolean insertUser (JdbcTemplate jdbcTemplate, WebUser webUser)
		{
			try
			{
				jdbcTemplate.update
						(
								"INSERT INTO WebUser VALUES(?, ?, ?, ?, ?)",
								webUser.getUuid(),
								webUser.getIsGoogleAccount() ? 1 : 0,
								webUser.getMail(),
								webUser.getPassword(),
								webUser.getType().ordinal()
						);
			}
			catch (Exception e)
			{
				System.out.println(e);
				return false;
			}
			return true;
		}
}
