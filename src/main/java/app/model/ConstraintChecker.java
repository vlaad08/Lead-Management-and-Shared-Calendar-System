package app.model;

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

  public static boolean checkDateAndTime(LocalDate date, Time startTime, Time endTime){
    if(!checkDate(date)){
      return false;
    }

    if(LocalDate.now().isEqual(date)){
      if(startTime.after(Time.valueOf(LocalTime.now()))){
        return true;
      }else return false;
    }else if(LocalDate.now().isBefore(date) && checkTime(startTime, endTime)){
      return true;
    }
    return false;
  }

  public static boolean checkFillOut(TextField textField)
  {
    return !textField.getText().equals("");
  }


}
