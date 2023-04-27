package pl.fryciarnia.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.fryciarnia.user.DbUser;

@AllArgsConstructor
@Data
public class AdpWorkerUser
{
  DbWorker worker;
  DbUser user;
}
