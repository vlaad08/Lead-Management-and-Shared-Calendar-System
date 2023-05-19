package app.model;

import javafx.scene.control.TextField;

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
    if (LocalDate.now().isEqual(date) || LocalDate.now().isBefore(date))
    {
      return true;
    }
    return false;
  }

  public static boolean checkDateAndTime(LocalDate date, String startTime, String endTime){
    if(!checkDate(date)){
      return false;
    }
    try{
      Time start=Time.valueOf(LocalTime.parse(startTime));
      Time end=Time.valueOf(LocalTime.parse(endTime));
    }catch (Exception e){
      return false;
    }

    if(LocalDate.now().isEqual(date)){
      if(LocalTime.parse(startTime).isAfter(LocalTime.now())){
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
