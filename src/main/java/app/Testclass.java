package app;

import app.JDBC.SQLConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Testclass
{
  public static void main(String[] args) throws Exception
  {
    SQLConnection connection = SQLConnection.getInstance();

    Date date =  Date.valueOf(LocalDate.of(2023,6,5));
    Time startTime = Time.valueOf(LocalTime.of(13,0,0));
    Time endTime = Time.valueOf(LocalTime.of(14,0,0));

    System.out.println(connection.getAvailableUser(date,startTime,endTime));
  }
}
