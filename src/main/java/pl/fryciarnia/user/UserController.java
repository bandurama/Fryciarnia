package pl.fryciarnia.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserController
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
        List<DbUser> all = UserController.fetchAll(jdbcTemplate);
        for (DbUser user : all)
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
