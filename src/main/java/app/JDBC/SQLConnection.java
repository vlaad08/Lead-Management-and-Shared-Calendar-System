package app.JDBC;

import app.shared.*;

import javax.crypto.CipherInputStream;
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
      if(!meetings.isEmpty())
      {
        return meetings;
      }
      return new ArrayList<>();
    }
  }

  public void createMeeting(Meeting meeting) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Meeting(title, description, date, startTime, endTime, email) VALUES(?, ?, ?, ?, ?, ?)"))
    {
      statement.setString(1, meeting.getTitle());
      statement.setString(2, meeting.getDescription());
      statement.setDate(3, meeting.getDate());
      statement.setTime(4, meeting.getStartTime());
      statement.setTime(5, meeting.getEndTime());
      statement.setString(6, meeting.getLeadEmail());
      statement.executeUpdate();
    }
  }

  public void createTask(Task task) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Task(title, description, dueDate, status, business_id) VALUES (?, ?, ?, ?, ?)"))
    {
      statement.setString(1, task.getTitle());
      statement.setString(2, task.getDescription());
      statement.setDate(3, task.getDate());
      statement.setString(4, task.getStatus());
      statement.setInt(5, task.getBusiness_id());
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
      if (!tasks.isEmpty())
      {
        return tasks;
      }
      return new ArrayList<>();
    }
  }

  public void editTask(Task newTask, Task oldTask) throws SQLException
  {

    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "update task set title = ?, description = ?, duedate = ?, status = ?, business_id = ? "
                + "where title = ? and description = ? and duedate = ? and status = ? and business_id = ?"))
    {

      statement.setString(1, newTask.getTitle());
      statement.setString(2, newTask.getDescription());
      statement.setDate(3, newTask.getDate());
      statement.setString(4, newTask.getStatus());
      statement.setInt(5, newTask.getBusiness_id());

      statement.setString(6, oldTask.getTitle());
      statement.setString(7, oldTask.getDescription());
      statement.setDate(8, oldTask.getDate());
      statement.setString(9, oldTask.getStatus());
      statement.setInt(10, oldTask.getBusiness_id());

      statement.executeUpdate();
    }

  }

  public ArrayList<User> getUsers() throws SQLException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from \"user\""))
    {
      ArrayList<User> users = new ArrayList<>();
      ResultSet set = statement.executeQuery();
      while(set.next())
      {
        boolean manager = false;

        String firstname = set.getString("firstname");
        String middleName = set.getString("middlename");

        if(middleName == null)
        {
          middleName = "";
        }

        String lastname = set.getString("lastname");
        String email = set.getString("email");
        String phone = set.getString("phone");
        String role = set.getString("role");

        if(role.equals("manager"))
        {
          manager = true;
        }

        String street = set.getString("street");
        int postalCode = set.getInt("postalcode");


        users.add(new User(firstname, middleName, lastname, email, phone, manager, street, postalCode));
      }
      if(!users.isEmpty())
      {
        return users;
      }
      return new ArrayList<>();
    }

  }


  public ArrayList<Business> getBusinesses() throws SQLException
  {
    try(Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement("select * from business"))
    {
      ArrayList<Business> businesses = new ArrayList<>();
      ResultSet set = statement.executeQuery();
      while(set.next())
      {
        String name = set.getString("businessname");
        int business_id = set.getInt("business_id");
        String street = set.getString("street");
        int postalCode = set.getInt("postalcode");
        businesses.add(new Business(name, business_id, street, postalCode));
      }
      return businesses;
    }
  }

  public void assignTask(Task task, String email) throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into assignment(title, duedate, business_id, email) values (?, ?, ?, ?)")
        )
    {
      statement.setString(1, task.getTitle());
      statement.setDate(2, task.getDate());
      statement.setInt(3, task.getBusiness_id());
      statement.setString(4, email);
      statement.executeUpdate();
    }
  }

  public ArrayList<String> getAssignedUsers(Task task) throws SQLException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from assignment"
            + " where title = ? and duedate = ? and business_id = ?"))
    {
      ArrayList<String> emails = new ArrayList<>();
      statement.setString(1, task.getTitle());
      statement.setDate(2, task.getDate());
      statement.setInt(3, task.getBusiness_id());
      ResultSet set = statement.executeQuery();
      while (set.next())
      {
        emails.add(set.getString("email"));
      }
      return emails;
    }
  }

  public void removeAssignedUsers(Task task) throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from assignment where title = ? and duedate = ? and business_id = ?")
        )
    {
      statement.setString(1, task.getTitle());
      statement.setDate(2, task.getDate());
      statement.setInt(3, task.getBusiness_id());
      statement.executeUpdate();
    }
  }

  public void removeTask(Task task) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from task where title =? and description = ? and duedate = ? and business_id = ?")
        )
    {
      statement.setString(1, task.getTitle());
      statement.setString(2, task.getDescription());
      statement.setDate(3, task.getDate());
      statement.setInt(4, task.getBusiness_id());
      statement.executeUpdate();
    }
  }

public void editMeeting(Meeting oldMeeting, Meeting newMeeting) throws SQLException{


  Connection connection = getConnection();
  PreparedStatement statement = connection.prepareStatement("update meeting set title = ?, description = ?,"
      + "  date = ?,  startTime = ?,  endTime = ?,  email = ? where title = ? and description = ? and date = ? and startTime = ? and endTime = ? and email = ?");

  statement.setString(1, newMeeting.getTitle());
  statement.setString(2, newMeeting.getDescription());
  statement.setDate(3, newMeeting.getDate());
  statement.setTime(4, newMeeting.getStartTime());
  statement.setTime(5, newMeeting.getEndTime());
  statement.setString(6, newMeeting.getLeadEmail());

  statement.setString(7, oldMeeting.getTitle());
  statement.setString(8, oldMeeting.getDescription());
  statement.setDate(9, oldMeeting.getDate());
  statement.setTime(10, oldMeeting.getStartTime());
  statement.setTime(11, oldMeeting.getEndTime());
  statement.setString(12, oldMeeting.getLeadEmail());

  statement.executeUpdate();

  }

  public void setAttendance(String email, Meeting meeting) throws SQLException
  {
    try(Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement("insert into attendance (title, date, starttime, endtime, email) values (?,?,?,?,?)"))
    {
      statement.setString(1,meeting.getTitle());
      statement.setDate(2, meeting.getDate());
      statement.setTime(3, meeting.getStartTime());
      statement.setTime(4, meeting.getEndTime());
      statement.setString(5,email);
      statement.executeUpdate();
    }
  }

  public ArrayList<Lead> getLeads() throws SQLException
  {
    try(Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement("select * from lead"))
    {
      ArrayList<Lead> leads = new ArrayList<>();
      ResultSet set = statement.executeQuery();
      while(set.next())
      {
        String firstname = set.getString("firstname");
        String middleName = set.getString("middlename");
        String lastname = set.getString("lastname");
        String email = set.getString("email");
        String phone = set.getString("phone");
        String title = set.getString("title");
        int business_id = set.getInt("business_id");

        leads.add(new Lead(firstname,middleName, lastname, email, phone, title, business_id));
      }
      return leads;
    }
  }



  public ArrayList<String> getAttendance(Meeting meeting) throws SQLException
  {
    try(Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement("select * from attendance"
        + " where title = ? and date = ? and starttime = ? and endtime = ?"))
    {
      ArrayList<String> emails = new ArrayList<>();
      statement.setString(1, meeting.getTitle());
      statement.setDate(2, meeting.getDate());
      statement.setTime(3, meeting.getStartTime());
      statement.setTime(4, meeting.getEndTime());
      ResultSet set = statement.executeQuery();
      while (set.next())
      {
        emails.add(set.getString("email"));
      }
      return emails;
    }
  }

  public void removeAttendance(Meeting meeting) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from attendance where"
            + " title = ? and date = ? and starttime = ? and endtime = ? "))
    {
      statement.setString(1, meeting.getTitle());
      statement.setDate(2, meeting.getDate());
      statement.setTime(3, meeting.getStartTime());
      statement.setTime(4, meeting.getEndTime());
      statement.executeUpdate();
    }

  }
}