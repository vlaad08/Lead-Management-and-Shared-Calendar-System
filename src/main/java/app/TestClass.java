package app;

import app.JDBC.SQLConnection;
import app.shared.Meeting;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TestClass
{
  public static void main(String[] args) throws SQLException
  {
    SQLConnection sqlConnection = SQLConnection.getInstance();
    sqlConnection.createMeeting("Tryout","This is a test", Date.valueOf(LocalDate.of(2023,4,29)),Time.valueOf(LocalTime.of(12,0,0)),Time.valueOf(LocalTime.of(13,0,0)));

  }
}
