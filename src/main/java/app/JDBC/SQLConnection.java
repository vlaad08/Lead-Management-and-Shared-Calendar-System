package app.JDBC;

import app.shared.*;

import java.sql.*;
import java.util.ArrayList;


public class SQLConnection
{
  private static SQLConnection instance;

  protected SQLConnection() throws SQLException
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

  Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=leadflow",
        "postgres", "1945");
  }

  public ArrayList<Object> getMeetings() throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "select * from meeting"))
    {
      ArrayList<Object> meetings = new ArrayList<>();
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



  public ArrayList<Object> getTasks() throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "select * from task"))
    {
      ArrayList<Object> tasks = new ArrayList<>();
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

  public ArrayList<Object> getUsers() throws SQLException {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" INNER JOIN address ON \"user\".street = address.street AND \"user\".postalcode = address.postalcode")) {
      ArrayList<Object> users = new ArrayList<>();
      ResultSet set = statement.executeQuery();
      while (set.next()) {



        String firstname = set.getString("firstname");
        String middleName = set.getString("middlename");

        if (middleName == null) {
          middleName = "";
        }
        String lastname = set.getString("lastname");
        String email = set.getString("email");
        String phone = set.getString("phone");
        boolean manager = set.getBoolean("role");
        String street = set.getString("street");
        int postalCode = set.getInt("postalcode");

        if(!email.equals("a@a.aa"))
        {
          users.add(new User(firstname, middleName, lastname, email, phone, manager, street, postalCode));
        }
      }
      return users;
    }
  }




  public ArrayList<Object> getBusinesses() throws SQLException
  {
    try(Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement("select * from business"))
    {
      ArrayList<Object> businesses = new ArrayList<>();
      ResultSet set = statement.executeQuery();
      while(set.next())
      {
        String name = set.getString("businessname");
        int business_id = set.getInt("business_id");
        String street = set.getString("street");
        int postalCode = set.getInt("postalcode");
        Business b = new Business(name, street, postalCode);
        b.setBusiness_id(business_id);
        businesses.add(b);
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

  public ArrayList<Object> getLeads() throws SQLException
  {
    try(Connection connection = getConnection();
    PreparedStatement statement = connection.prepareStatement("select * from lead"))
    {
      ArrayList<Object> leads = new ArrayList<>();
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
        String status = set.getString("status");
        leads.add(new Lead(firstname,middleName, lastname, email, phone, title, business_id, getBusinessNameByID(business_id),status));
      }
      return leads;
    }
  }

  public String getBusinessNameByID(int id) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select businessname from business where business_id = ?"))
    {
      statement.setInt(1, id);
      ResultSet set = statement.executeQuery();
      String name = "";
      if(set.next())
      {
        name = set.getString("businessname");
      }
      return name;
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

  public void createLead(Lead lead) throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Lead(firstName, middleName, lastName, email, phone, title, business_id) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
      statement.setString(1, lead.getFirstname());
      statement.setString(2, lead.getMiddleName());
      statement.setString(3, lead.getLastname());
      statement.setString(4, lead.getEmail());
      statement.setString(5, lead.getPhone());
      statement.setString(6, lead.getTitle());
      statement.setInt(7, lead.getBusiness_id());
      statement.executeUpdate();
    }
  }

  public void createUser(User user, String password) throws SQLException
  {
    try(Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT Into \"user\"(firstname, middlename, lastname, email, password, phone, role, street, postalcode) VALUES (?,?,?,?,?,?,?,?,?)"))
    {
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getMiddleName());
      statement.setString(3, user.getLastName());
      statement.setString(4, user.getEmail());
      statement.setString(5, password);
      statement.setString(6, user.getPhone());
      statement.setBoolean(7, user.isManager());
      statement.setString(8, user.getStreet());
      statement.setInt(9, user.getPostalCode());
      statement.executeUpdate();
    }
  }

  public void createAddress(Address address) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into address(street,city,country,postalcode) values(?,?,?,?)")
            )
    {
      statement.setString(1, address.getStreet());
      statement.setString(2, address.getCity());
      statement.setString(3, address.getCountry());
      statement.setInt(4, address.getPostalCode());
      statement.executeUpdate();
    }
  }

  public Address getAddress(Address address) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from address where street = ? and city = ? and country = ? and postalcode = ?")
            )
    {
      statement.setString(1, address.getStreet());
      statement.setString(2, address.getCity());
      statement.setString(3, address.getCountry());
      statement.setInt(4, address.getPostalCode());
      ResultSet set = statement.executeQuery();
      Address a = null;
      if (set.next())
      {
        String street = set.getString("street");
        String city = set.getString("city");
        String country = set.getString("country");
        int postalCode = set.getInt("postalcode");
        a = new Address(street, city, country, postalCode);
      }

      return a;
    }
  }

  public void createBusiness(Business business) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into business(businessname, street, postalcode) values (?,?,?)"))
    {
        statement.setString(1, business.getName());
        statement.setString(2, business.getStreet());
        statement.setInt(3, business.getPostalCode());
        statement.executeUpdate();
    }
  }

  public int getBusinessID(Business business) throws SQLException
  {
    try (
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select business_id from business where businessname = ? and street = ? and postalcode = ?")
        )
    {
      statement.setString(1, business.getName());
      statement.setString(2, business.getStreet());
      statement.setInt(3, business.getPostalCode());
      ResultSet set = statement.executeQuery();
      int id = 0;
      if(set.next())
      {
        id =  set.getInt("business_id");
      }
      return id;
    }
  }

  public void editLead(Lead oldLead, Lead newLead) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("update lead set firstname = ?, middlename = ?, lastname = ?,"
                + "email = ?, phone = ?, title = ?, business_id = ? where firstname = ? and middlename = ? and lastname = ? and "
                + "email = ? and phone = ? and title = ? and business_id = ?")
            )
    {
      statement.setString(1, newLead.getFirstname());
      statement.setString(2, newLead.getMiddleName());
      statement.setString(3, newLead.getLastname());
      statement.setString(4, newLead.getEmail());
      statement.setString(5, newLead.getPhone());
      statement.setString(6, newLead.getTitle());
      statement.setInt(7, newLead.getBusiness_id());

      statement.setString(8, oldLead.getFirstname());
      statement.setString(9, oldLead.getMiddleName());
      statement.setString(10, oldLead.getLastname());
      statement.setString(11, oldLead.getEmail());
      statement.setString(12, oldLead.getPhone());
      statement.setString(13, oldLead.getTitle());
      statement.setInt(14, oldLead.getBusiness_id());

      statement.executeUpdate();
    }
  }

  public ArrayList<Meeting> getMeetingsByLead(Lead lead) throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from meeting where email = ?")
        )
    {
      ArrayList<Meeting> meetings = new ArrayList<>();
      statement.setString(1, lead.getEmail());
      ResultSet set = statement.executeQuery();
      while (set.next())
      {
        String title = set.getString("title");
        String description = set.getString("description");
        Date date = set.getDate("date");
        Time startTime = set.getTime("starttime");
        Time endTime = set.getTime("endtime");
        String email = set.getString("email");
        meetings.add(
            new Meeting(title, description, date, startTime, endTime, email));
      }
      return meetings;
    }
  }

  public void removeMeeting(Meeting meeting) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from meeting where title = ? and date = ? and starttime = ? and endtime = ? and email = ?"))
    {
      statement.setString(1, meeting.getTitle());
      statement.setDate(2, meeting.getDate());
      statement.setTime(3,meeting.getStartTime());
      statement.setTime(4,meeting.getEndTime());
      statement.setString(5, meeting.getLeadEmail());
      statement.executeUpdate();
    }
  }

  public void removeLead(Lead lead) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from lead where firstname = ? and lastname = ? and email = ? and phone = ? and title = ? and business_id = ?")
            )
    {
      statement.setString(1, lead.getFirstname());
      statement.setString(2, lead.getLastname());
      statement.setString(3, lead.getEmail());
      statement.setString(4, lead.getPhone());
      statement.setString(5, lead.getTitle());
      statement.setInt(6, lead.getBusiness_id());
      statement.executeUpdate();
    }
  }

  public User getUserByEmail(String email) throws SQLException {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"user\" INNER JOIN address ON \"user\".street = address.street AND \"user\".postalcode = address.postalcode WHERE email = ?")) {
      statement.setString(1, email);
      ResultSet set = statement.executeQuery();
      User user = null;
      if (set.next()) {


        String firstname = set.getString("firstname");
        String middleName = set.getString("middlename");

        if (middleName == null) {
          middleName = "";
        }

        String lastname = set.getString("lastname");
        String e = set.getString("email");
        String phone = set.getString("phone");
        boolean manager = set.getBoolean("role");
        String street = set.getString("street");
        int postalCode = set.getInt("postalcode");


        user = new User(firstname, middleName, lastname, e, phone, manager, street, postalCode);
      }
      return user;
    }
  }

  public boolean logIn(String email, String password) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from \"user\" where email = ? and password = ?")
            )
    {
      statement.setString(1, email);
      statement.setString(2, password);

      ResultSet set = statement.executeQuery();

      if(set.next())
      {
        return set.getString("email").equals(email) && set.getString("password")
            .equals(password);
      }
      return false;
    }
  }

  public ArrayList<Object> getMeetingsByUser(User user) throws  SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "select * from meeting m join attendance a on m.title = a.title and m.date = a.date and m.starttime = a.starttime and m.endtime = a.endtime\n"
                + " where a.email = ?"))
    {

      statement.setString(1, user.getEmail());
      ArrayList<Object> meetings = new ArrayList<>();
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

  public ArrayList<Object> getTasksByUser(User user) throws SQLException
  {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "select * from task t join assignment a on t.title = a.title and t.duedate = a.duedate and t.business_id = a.business_id\n"
                + "where a.email = ?"))
    {
      ArrayList<Object> tasks = new ArrayList<>();
      statement.setString(1, user.getEmail());
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

  public String getUserPassword(String oldEmail) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("select password from \"user\" where email = ?")
            )
    {
      statement.setString(1, oldEmail);
      ResultSet set = statement.executeQuery();
      if(set.next())
      {
        return set.getString("password");
      }
      return null;
    }
  }

  public void removeUser(String email) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement =connection.prepareStatement("delete from \"user\" where email = ?")
            )
    {
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }

  public void removeAssignmentsForUser(String email) throws SQLException
  {
    try(
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "delete from assignment where email = ?")
        )
    {
      statement.setString(1, email);
      statement.executeUpdate();
    }
  }

  public void removeAttendanceForUser(String email) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from attendance where email = ?"))
    {
      statement.setString(1, email);
      statement.executeUpdate();

    }
  }

  public void editUser(User oldUser, User newUser, String password) throws SQLException
  {
    try
        (
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "update \"user\" set firstname = ?, middlename = ?, lastname = ?, email = ?, password = ?, phone = ?, role = ?, street = ?, postalcode = ? where "
                    + "email = ?"
            )
            )
    {
      statement.setString(1, newUser.getFirstName());
      statement.setString(2, newUser.getMiddleName());
      statement.setString(3, newUser.getLastName());
      statement.setString(4, newUser.getEmail());
      statement.setString(5, password);
      statement.setString(6, newUser.getPhone());
      statement.setBoolean(7, newUser.isManager());
      statement.setString(8, newUser.getStreet());
      statement.setInt(9, newUser.getPostalCode());
      statement.setString(10, oldUser.getEmail());
      statement.executeUpdate();
    }
  }

  public Object getAddress(User obj) throws SQLException
  {
    try( Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from address where street = ? and postalcode = ?")

        )
    {
      statement.setString(1, obj.getStreet());
      statement.setInt(2, obj.getPostalCode());
      ResultSet set = statement.executeQuery();
      if(set.next())
      {
        String city = set.getString("city");
        String country = set.getString("country");
        return new Address(obj.getStreet(), city, country, obj.getPostalCode());
      }
      return null;
    }
  }

  public void removeAddress(Address obj) throws SQLException
  {
    try
        (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("delete from address where street = ? and city = ? and country = ? and postalcode = ? "))
    {
      statement.setString(1, obj.getStreet());
      statement.setString(2, obj.getCity());
      statement.setString(3, obj.getCountry());
      statement.setInt(4, obj.getPostalCode());
      statement.executeUpdate();
    }
  }

  //  public ArrayList<Object> getAttendanceByUser(User oldObj)
//  {
//
//  }
}