package app.shared;



import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public record Meeting(String title, String description, Date date, Time startTime, Time endTime,String email) implements Serializable
{
  @Override public String toString()
  {
    return "Meeting: \n Title: " + title + "\n Description: " + description + "\n Date: " + date + "\n Start time: " + startTime + "\n End time: " + endTime + "\n"+email;
  }
}
