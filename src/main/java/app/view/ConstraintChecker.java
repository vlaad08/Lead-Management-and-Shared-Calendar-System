package app.view;

import javafx.scene.control.TextField;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConstraintChecker
{
  public static boolean checkTime(Time startTime, Time endTime)
  {

    if (endTime.before(startTime))
    {
      return false;
    }
    return true;
  }

  public static boolean checkInt(String input){
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static boolean checkDate(LocalDate date)
  {
    if (LocalDate.now().isEqual(date) || LocalDate.now().isBefore(date))
    {
      return true;
    }
    return false;
  }

  public static boolean checkFillOut(TextField textField)
  {
    return !textField.getText().equals("");
  }


}
