package app;

import app.JDBC.SQLConnection;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestClass
{
  public static void main(String[] args) throws SQLException
  {
    SQLConnection sqlConnection = SQLConnection.getInstance();
    sqlConnection.createMeetingInBusiness("Firing somebody", "Get ready for us to fire an employee, the one with no value to the business",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(10,30,0)),
        Time.valueOf(LocalTime.of(10,45,0)), 2);
  }
}
