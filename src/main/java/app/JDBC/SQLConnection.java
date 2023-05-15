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
    if (instance == null)
    {
      instance = new SQLConnection();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=leadflow",
        "postgres", "1945");
  }

  public ArrayList<Meeting> getMeetings() throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "select * from meeting"))
    {
      ArrayList<Meeting> meetings = new ArrayList<>();
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Date date = resultSet.getDate("date");
        Time startTime = resultSet.getTime("starttime");
        Time endTime = resultSet.getTime("endtime");
        String email = resultSet.getString("email");
        meetings.add(
            new Meeting(title, description, date, startTime, endTime, email));
      }
      return meetings;
    }
  }

  public void createMeeting(Meeting meeting) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Meeting(title, description, date, startTime, endTime, email) VALUES(?, ?, ?, ?, ?, ?)"))
    {
      statement.setString(1, meeting.title());
      statement.setString(2, meeting.description());
      statement.setDate(3, meeting.date());
      statement.setTime(4, meeting.startTime());
      statement.setTime(5, meeting.endTime());
      statement.setString(6, meeting.email());
      statement.executeUpdate();
    }
  }

  public void createTask(Task task) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Task(title, description, dueDate, status, business_id) VALUES (?, ?, ?, ?, ?)"))
    {
      statement.setString(1, task.title());
      statement.setString(2, task.description());
      statement.setDate(3, task.date());
      statement.setString(4, task.status());
      statement.setInt(5, task.business_id());
      statement.executeUpdate();
    }
  }

  public ArrayList<Task> getTasks() throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "select * from task"))
    {
      ArrayList<Task> tasks = new ArrayList<>();
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String title = resultSet.getString("title");
        String description = resultSet.getString("description");
        Date date = resultSet.getDate("dueDate");
        String status = resultSet.getString("status");
        int businessId = resultSet.getInt("business_id");
        tasks.add(new Task(title, description, date, status, businessId));
      }
      return tasks;
    }
  }

  public void editTask(Task newTask, Task oldTask) throws SQLException
  {

    Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement(
        "update task set title = ?, description = ?, duedate = ?, status = ?, business_id = ? "
            + "where title = ? and description = ? and duedate = ? and status = ? and business_id = ?");

    statement.setString(1, newTask.title());
    statement.setString(2, newTask.description());
    statement.setDate(3, newTask.date());
    statement.setString(4, newTask.status());
    statement.setInt(5, newTask.business_id());

    statement.setString(6, oldTask.title());
    statement.setString(7, oldTask.description());
    statement.setDate(8, oldTask.date());
    statement.setString(9, oldTask.status());
    statement.setInt(10, oldTask.business_id());

    statement.executeUpdate();

  }




public void editMeeting(Meeting oldMeeting, Meeting newMeeting) throws SQLException{


  Connection connection = getConnection();
  PreparedStatement statement = connection.prepareStatement("update meeting set title = ?, set description = ?,"
      + " set date = ?, set startTime = ?, set endTime = ?, set email = ? where title = ?, description = ?, date = ?, startTime = ?, endTime = ?, email = ?");

  statement.setString(1, newMeeting.title());
  statement.setString(1, newMeeting.description());
  statement.setDate(1, newMeeting.date());
  statement.setTime(1, newMeeting.startTime());
  statement.setTime(1, newMeeting.endTime());
  statement.setString(1, newMeeting.email());

  statement.setString(1, oldMeeting.title());
  statement.setString(1, oldMeeting.description());
  statement.setDate(1, oldMeeting.date());
  statement.setTime(1, oldMeeting.startTime());
  statement.setTime(1, oldMeeting.endTime());
  statement.setString(1, oldMeeting.email());

  statement.executeUpdate();

  }

}