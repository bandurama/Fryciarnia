package pl.fryciarnia.user;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserController
{
    public static List<DbUser> fetchAll (JdbcTemplate jdbcTemplate)
    {
        return jdbcTemplate.query
        (
					"SELECT * FROM DbUser",
					BeanPropertyRowMapper.newInstance(DbUser.class)
        );
    }

    public static DbUser getDbUserByUUID (JdbcTemplate jdbcTemplate, String uuid)
    {
        List<DbUser> all = UserController.fetchAll(jdbcTemplate);
        for (DbUser user : all)
            if (user.getUuid().equals(uuid))
                return user;

        return null;
    }


    public static boolean updateUser (JdbcTemplate jdbcTemplate, DbUser dbUser)
    {
        try
        {
            jdbcTemplate.update
            (
							"UPDATE DbUser SET isGoogleAccount = ?, mail = ?, password = ?, type = ?, name = ?, surname = ? WHERE uuid = ?",
							dbUser.getIsGoogleAccount(),
							dbUser.getMail(),
							dbUser.getPassword(),
							dbUser.getType(),
							dbUser.getName(),
							dbUser.getSurname(),
							dbUser.getUuid()
						);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
        return true;
    }

		public static boolean insertUser (JdbcTemplate jdbcTemplate, DbUser webUser)
		{
			try
			{
				jdbcTemplate.update
						(
								"INSERT INTO DbUser VALUES(?, ?, ?, ?, ?, ?, ?)",
								webUser.getUuid(),
								webUser.getIsGoogleAccount() ? 1 : 0,
								webUser.getMail(),
								webUser.getName(),
								webUser.getSurname(),
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
