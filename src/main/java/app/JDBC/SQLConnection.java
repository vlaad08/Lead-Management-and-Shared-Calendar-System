package app.JDBC;

import app.shared.Meeting;

import java.sql.*;
import java.util.ArrayList;

public class SQLConnection
{
  private static SQLConnection instance;

  private SQLConnection() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public synchronized static SQLConnection getInstance() throws SQLException
  {
    if(instance == null)
    {
      instance = new SQLConnection();

    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=leadflow", "postgres", "password");
  }

  public ArrayList<Meeting> getMeetings() throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from meeting")
        )
    {
      ArrayList<Meeting> meetings = new ArrayList<>();
      ResultSet set = statement.getResultSet();
      while(set.next())
      {
        String title = set.getString("title");
        String description = set.getString("description");
        Date date = set.getDate("date");
        Time startTime = set.getTime("starttime");
        Time endTime = set.getTime("endtime");
        meetings.add(new Meeting(title, description, date, startTime, endTime));
      }
      return meetings;
    }
  }

  public void createMeeting(String title, String description, Date date, Time startTime, Time endTime) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into meeting(title, description, date, starttime, endtime) values (?,?,?,?,?)")
            )
    {
      statement.setString(1, title);
      statement.setString(2, description);
      statement.setDate(3, date);
      statement.setTime(4, startTime);
      statement.setTime(5, endTime);
      statement.executeUpdate();
    }
  }


  public void removeMeeting() throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from meeting where date < now() - interval '7 days'")
        )
    {
      statement.executeUpdate();
    }
  }
  

}
