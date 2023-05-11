package app.JDBC;

import app.shared.Meeting;
import app.shared.Task;

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
    if(instance == null) {
      instance = new SQLConnection();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=leadflow", "postgres", "password");
  }

  /*
  public ArrayList<Meeting> getMeetingsByBusinessId(int business_id) throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from meeting where business_id = ?")) {
        ArrayList<Meeting> meetings = new ArrayList<>();
        statement.setInt(1, business_id);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
          String title = resultSet.getString("title");
          String description = resultSet.getString("description");
          Date date = resultSet.getDate("date");
          Time startTime = resultSet.getTime("starttime");
          Time endTime = resultSet.getTime("endtime");
          meetings.add(new Meeting(title, description, date, startTime, endTime));
        }
        if(!meetings.isEmpty())
        {
          return meetings;
        }
        return null;
    }
  }

   */

  public ArrayList<Meeting> getMeetings() throws SQLException{
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from meeting")) {
      ArrayList<Meeting> meetings = new ArrayList<>();
      ResultSet resultSet = statement.executeQuery();
      while(resultSet.next())
      {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Date date = resultSet.getDate("date");
        Time startTime = resultSet.getTime("starttime");
        Time endTime = resultSet.getTime("endtime");
        String email = resultSet.getString("email");
        meetings.add(new Meeting(title, description, date, startTime, endTime, email));
      }
      if(!meetings.isEmpty())
      {
        return meetings;
      }
      return null;
    }
  }

  public void createMeeting(String title, String description, Date date, Time startTime, Time endTime, String email) throws SQLException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Meeting(title, description, date, startTime, endTime, email) VALUES(?, ?, ?, ?, ?, ?)"))   {
      statement.setString(1, title);
      statement.setString(2, description);
      statement.setDate(3, date);
      statement.setTime(4, startTime);
      statement.setTime(5, endTime);
      statement.setString(6, email);
      statement.executeUpdate();
    }
  }

  public void addTask(Task task) throws SQLException{
    try(
      Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement("INSERT INTO Task(title, description, dueDate, status, business_id) VALUES (?, ?, ?, ?, ?")) {
        statement.setString(1, task.getTitle());
        statement.setString(2, task.getDescription());
        statement.setDate(3,task.getDate());
        statement.setString(4,task.getStatus());
        statement.setInt(5,task.getBusinessId());
        statement.executeUpdate();
      }
  }
}

