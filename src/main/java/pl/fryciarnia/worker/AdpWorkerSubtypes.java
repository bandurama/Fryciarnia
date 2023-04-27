package pl.fryciarnia.worker;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdpWorkerSubtypes
{
  List<AdpWorkerUser> hardware;
  List<AdpWorkerUser> people;

  public AdpWorkerSubtypes ()
  {
    hardware = new ArrayList<>();
    people = new ArrayList<>();
  }
}
