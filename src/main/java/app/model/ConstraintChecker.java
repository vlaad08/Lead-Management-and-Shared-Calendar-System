package app.model;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConstraintChecker
{
  public static boolean checkTime(String startTime, String endTime)
  {
    Time start=Time.valueOf(LocalTime.parse(startTime));
    Time end=Time.valueOf(LocalTime.parse(endTime));
    if (end.before(start))
    {
      return false;
    }
    return true;
  }

  public static boolean checkDate(LocalDate date)
  {
    if (date.isBefore(LocalDate.now()))
    {
      return false;
    }
    return true;
  }

}
