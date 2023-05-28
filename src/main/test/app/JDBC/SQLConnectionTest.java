package app.JDBC;

import app.shared.*;
import org.checkerframework.checker.units.qual.A;
import org.mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.transformers.MockTransformer;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SQLConnectionTest {
  private SQLConnection sqlConnection;
  private Connection connection;
  private PreparedStatement statement;
  private ResultSet resultSet;
  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;
  @Captor
  private ArgumentCaptor<Date> dateArgumentCaptor;
  @Captor
  private ArgumentCaptor<Time> timeArgumentCaptor;
  @Captor
  private ArgumentCaptor<Integer> integerArgumentCaptor;
  @Captor
  private ArgumentCaptor<Boolean> booleanArgumentCaptor;


  @BeforeEach
  void setUp() throws SQLException
  {
    sqlConnection = Mockito.spy(SQLConnection.getInstance());
    connection = Mockito.mock(Connection.class);
    statement = Mockito.mock(PreparedStatement.class);
    resultSet=Mockito.mock(ResultSet.class);
    stringArgumentCaptor=ArgumentCaptor.forClass(String.class);
    dateArgumentCaptor=ArgumentCaptor.forClass(Date.class);
    timeArgumentCaptor=ArgumentCaptor.forClass(Time.class);
    integerArgumentCaptor=ArgumentCaptor.forClass(Integer.class);
    booleanArgumentCaptor=ArgumentCaptor.forClass(Boolean.class);

    Mockito.when(sqlConnection.getConnection()).thenReturn(connection);
    Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
    Mockito.when(statement.executeQuery()).thenReturn(resultSet);
  }

  @Test
  void sqlConnection_is_singleton() throws SQLException {
    SQLConnection instance1 = SQLConnection.getInstance();
    SQLConnection instance2 = SQLConnection.getInstance();

    assertSame(instance1,instance2);
  }

  @Test
  void connection_is_existing() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
    SQLConnection sqlConnection = SQLConnection.getInstance();

    Method getConnectionMethod = SQLConnection.class.getDeclaredMethod("getConnection");
    getConnectionMethod.setAccessible(true);
    Connection connection = (Connection) getConnectionMethod.invoke(sqlConnection);

    assertNotNull(connection);
  }

  @Test
  void get_Meetings_gives_back_an_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class,sqlConnection.getMeetings().getClass());
  }

  @Test
  void get_Meetings_gives_back_all_meetings() throws Exception
  {
    Mockito.when(sqlConnection.getMeetings()).thenReturn(new ArrayList<>(
        List.of(new Meeting("Meeting1", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
                Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com"),
            new Meeting("Meeting2", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
                Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le2@gmail.com"))
    ));
    ArrayList<Object> meetings = sqlConnection.getMeetings();
    Mockito.verify(connection).prepareStatement("select * from meeting");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,meetings.size());

    Meeting meeting1 = (Meeting) meetings.get(0);
    assertEquals(meeting1.getTitle(),"Meeting1");
    assertEquals(meeting1.getDescription(),"");
    assertEquals(meeting1.getDate(),Date.valueOf(LocalDate.of( 2023, 5, 22)));
    assertEquals(meeting1.getStartTime(),Time.valueOf(LocalTime.of(7, 0)));
    assertEquals(meeting1.getEndTime(),Time.valueOf(LocalTime.of(11,0)));
    assertEquals(meeting1.getLeadEmail(),"le1@gmail.com");
    Meeting meeting2 = (Meeting) meetings.get(1);
    assertEquals(meeting2.getTitle(),"Meeting2");
    assertEquals(meeting2.getDescription(),"");
    assertEquals(meeting2.getDate(),Date.valueOf(LocalDate.of( 2023, 5, 22)));
    assertEquals(meeting2.getStartTime(),Time.valueOf(LocalTime.of(7, 0)));
    assertEquals(meeting2.getEndTime(),Time.valueOf(LocalTime.of(11,0)));
    assertEquals(meeting2.getLeadEmail(),"le2@gmail.com");
  }

  @Test
  void create_meeting_saves_the_meeting_in_the_database() throws SQLException
  {
    Date date= Date.valueOf(LocalDate.of( 2023, 5, 22));
    Time startTime= Time.valueOf(LocalTime.of(7, 0));
    Time endTime= Time.valueOf(LocalTime.of(11,0));
    Meeting meeting= new Meeting("Meeting", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le@gmail.com");


    sqlConnection.createMeeting(meeting);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(3), dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(4), timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(5), timeArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6), stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("INSERT INTO Meeting(title, description, date, startTime, endTime, email) VALUES(?, ?, ?, ?, ?, ?)", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Meeting", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("", stringArgumentCaptor.getAllValues().get(2));
    assertEquals(date, dateArgumentCaptor.getValue());
    assertEquals(startTime, timeArgumentCaptor.getAllValues().get(0));
    assertEquals(endTime, timeArgumentCaptor.getAllValues().get(1));
    assertEquals("le@gmail.com", stringArgumentCaptor.getAllValues().get(3));
  }

  @Test
  void get_Tasks_gives_back_an_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class, sqlConnection.getTasks().getClass());
  }

  @Test
  void get_Tasks_gives_back_all_tasks() throws SQLException
  {
    Mockito.when(sqlConnection.getTasks()).thenReturn(new ArrayList<>(List.of(new Task("Task1","", Date.valueOf(LocalDate.of( 2023, 5, 22)),"To do", 7456),new Task("Task2","",Date.valueOf(LocalDate.of( 2023, 5, 24)),"Ready", 7456))));

    ArrayList<Object> tasks = sqlConnection.getTasks();
    Mockito.verify(connection).prepareStatement("select * from task");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,tasks.size());

    Task task1 = (Task) tasks.get(0);
    assertEquals(task1.getTitle(),"Task1");
    assertEquals(task1.getDescription(),"");
    assertEquals(task1.getDate(),Date.valueOf(LocalDate.of( 2023, 5, 22)));
    assertEquals(task1.getStatus(),"To do");
    assertEquals(task1.getBusiness_id(),7456);
    Task task2 = (Task) tasks.get(1);
    assertEquals(task2.getTitle(),"Task2");
    assertEquals(task2.getDescription(),"");
    assertEquals(task2.getDate(),Date.valueOf(LocalDate.of( 2023, 5, 24)));
    assertEquals(task2.getStatus(),"Ready");
    assertEquals(task2.getBusiness_id(),7456);
  }

  @Test
  void create_Task_saves_task_in_the_database() throws SQLException
  {
    Date date= Date.valueOf(LocalDate.of( 2023, 5, 22));
    Task task= new Task("Task","", date,"To do", 7456);

    sqlConnection.createTask(task);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(3), dateArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4), stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(5), integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("INSERT INTO Task(title, description, dueDate, status, business_id) VALUES (?, ?, ?, ?, ?)", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Task", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("", stringArgumentCaptor.getAllValues().get(2));
    assertEquals(date, dateArgumentCaptor.getValue());
    assertEquals("To do", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(7456, integerArgumentCaptor.getValue());
  }

  @Test
  void edit_Task_edits_the_task_in_the_database() throws SQLException
  {
    Date date= Date.valueOf(LocalDate.of( 2023, 5, 22));
    Task oldTask= new Task("Task","", date,"To do", 7456);
    Task newTask= new Task("Task","", date,"In Progress", 7456);

    sqlConnection.editTask(newTask,oldTask);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(3), dateArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4), stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(5), integerArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(7), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(8), dateArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(9), stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(10), integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("update task set title = ?, description = ?, duedate = ?, status = ?, business_id = ? "
        + "where title = ? and description = ? and duedate = ? and status = ? and business_id = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Task", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("", stringArgumentCaptor.getAllValues().get(2));
    assertEquals(date, dateArgumentCaptor.getAllValues().get(0));
    assertEquals("In Progress", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(7456, integerArgumentCaptor.getValue());
    assertEquals("Task", stringArgumentCaptor.getAllValues().get(4));
    assertEquals("", stringArgumentCaptor.getAllValues().get(5));
    assertEquals(date, dateArgumentCaptor.getAllValues().get(1));
    assertEquals("To do", stringArgumentCaptor.getAllValues().get(6));
    assertEquals(7456, integerArgumentCaptor.getValue());
  }

  @Test
  void get_Users_returns_an_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class, sqlConnection.getUsers().getClass());
  }

  @Test
  void get_Users_gives_back_all_users() throws SQLException
  {
    Mockito.when(sqlConnection.getUsers()).thenReturn(new ArrayList<>(List.of(new User("Test1", null,"User", "testuser1@gmail.com","+4511223344",false,"Street 1",4433),new User("Test2", null,"User", "testuser2@gmail.com","+4522334455",true,"Street 2",4433))));

    ArrayList<Object> users=sqlConnection.getUsers();
    Mockito.verify(connection).prepareStatement("SELECT * FROM \"user\" INNER JOIN address ON \"user\".street = address.street AND \"user\".postalcode = address.postalcode");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,users.size());

    User user1= (User) users.get(0);
    assertEquals(user1.getFirstName(),"Test1");
    assertEquals(user1.getMiddleName(),null);
    assertEquals(user1.getLastName(),"User");
    assertEquals(user1.getEmail(),"testuser1@gmail.com");
    assertEquals(user1.getPhone(),"+4511223344");
    assertEquals(user1.isManager(),false);
    assertEquals(user1.getStreet(),"Street 1");
    assertEquals(user1.getPostalCode(),4433);

    User user2= (User) users.get(1);
    assertEquals(user2.getFirstName(),"Test2");
    assertEquals(user2.getMiddleName(),null);
    assertEquals(user2.getLastName(),"User");
    assertEquals(user2.getEmail(),"testuser2@gmail.com");
    assertEquals(user2.getPhone(),"+4522334455");
    assertEquals(user2.isManager(),true);
    assertEquals(user2.getStreet(),"Street 2");
    assertEquals(user2.getPostalCode(),4433);
  }

  @Test
  void get_Businesses_returns_an_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class, sqlConnection.getBusinesses().getClass());
  }

  @Test
  void get_Businesses_gives_back_all_businesses() throws SQLException
  {
    Mockito.when(sqlConnection.getBusinesses()).thenReturn(new ArrayList<>(List.of(new Business("Test1", "Street 1",1122), new Business("Test2","Street 2", 1122 ))));

    ArrayList<Object> businesses=sqlConnection.getBusinesses();
    Mockito.verify(connection).prepareStatement("select * from business");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,businesses.size());

    Business business1= (Business) businesses.get(0);
    assertEquals(business1.getName(), "Test1");
    assertEquals(business1.getStreet(), "Street 1");
    assertEquals(business1.getPostalCode(), 1122);
    Business business2= (Business) businesses.get(1);
    assertEquals(business2.getName(), "Test2");
    assertEquals(business2.getStreet(), "Street 2");
    assertEquals(business2.getPostalCode(), 1122);
  }

  @Test
  void assign_Task_changes_the_assignment_table() throws SQLException
  {
    Date date= Date.valueOf(LocalDate.of( 2023, 5, 22));
    Task task= new Task("Task","", date,"To do", 7456);
    String email="lead@gmail.com";
    sqlConnection.assignTask(task,email);
    Mockito.verify(connection).prepareStatement("insert into assignment(title, duedate, business_id, email) values (?, ?, ?, ?)");
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(2),dateArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(3),integerArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("Task",stringArgumentCaptor.getAllValues().get(0));
    assertEquals(date,dateArgumentCaptor.getValue());
    assertEquals(7456,integerArgumentCaptor.getValue());
    assertEquals("lead@gmail.com",stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void removing_the_attendance_of_a_meeting_updates_the_table() throws SQLException
  {
    Date date= Date.valueOf(LocalDate.of( 2023, 5, 22));
    Task task= new Task("Task","", date,"To do", 7456);

    sqlConnection.removeAssignedUsers(task);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(2),dateArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(3),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals(stringArgumentCaptor.getAllValues().get(0),"delete from assignment where title = ? and duedate = ? and business_id = ?");
    assertEquals(stringArgumentCaptor.getAllValues().get(1),"Task");
    assertEquals(dateArgumentCaptor.getValue(),date);
    assertEquals(integerArgumentCaptor.getValue(),7456);
  }

  @Test
  void remove_Task_deletes_task_from_the_database() throws SQLException
  {
    Date date= Date.valueOf(LocalDate.of( 2023, 5, 22));
    Task task= new Task("Task","", date,"To do", 7456);

    sqlConnection.removeTask(task);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(3),dateArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(4),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals(stringArgumentCaptor.getAllValues().get(0),"delete from task where title =? and description = ? and duedate = ? and business_id = ?");
    assertEquals(stringArgumentCaptor.getAllValues().get(1),"Task");
    assertEquals(stringArgumentCaptor.getAllValues().get(2),"");
    assertEquals(dateArgumentCaptor.getValue(),date);
    assertEquals(integerArgumentCaptor.getValue(),7456);
  }

  @Test
  void edit_Meeting_edits_the_meeting_in_the_database() throws SQLException
  {
    Meeting oldMeeting= new Meeting("Meeting1", "", Date.valueOf(LocalDate.of( 2023, 5, 19)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com");
    Meeting newMeeting= new Meeting("Meeting2", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le2@gmail.com");

    sqlConnection.editMeeting(oldMeeting,newMeeting);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(3), dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(4), timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(5), timeArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(7), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(8), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(9), dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(10), timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(11), timeArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(12), stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("update meeting set title = ?, description = ?,"
        + "  date = ?,  startTime = ?,  endTime = ?,  email = ? where title = ? and description = ? and date = ? and startTime = ? and endTime = ? and email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Meeting2", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("", stringArgumentCaptor.getAllValues().get(2));
    assertEquals(Date.valueOf(LocalDate.of( 2023, 5, 22)), dateArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(7, 0)), timeArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(11, 0)), timeArgumentCaptor.getAllValues().get(1));
    assertEquals("le2@gmail.com", stringArgumentCaptor.getAllValues().get(3));

    assertEquals("Meeting1", stringArgumentCaptor.getAllValues().get(4));
    assertEquals("", stringArgumentCaptor.getAllValues().get(5));
    assertEquals(Date.valueOf(LocalDate.of( 2023, 5, 19)), dateArgumentCaptor.getAllValues().get(1));
    assertEquals(Time.valueOf(LocalTime.of(7, 0)), timeArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(11, 0)), timeArgumentCaptor.getAllValues().get(1));
    assertEquals("le1@gmail.com", stringArgumentCaptor.getAllValues().get(6));
  }

  @Test
  void setting_attendance_will_insert_into_attendance_table()
      throws SQLException
  {
    String email="lead@gmail.com";
    Meeting meeting= new Meeting("Meeting", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), null);
    sqlConnection.setAttendance(email,meeting);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(2), dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(3), timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(4), timeArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5), stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("insert into attendance (title, date, starttime, endtime, email) values (?,?,?,?,?)", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Meeting", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(Date.valueOf(LocalDate.of( 2023, 5, 22)), dateArgumentCaptor.getValue());
    assertEquals(Time.valueOf(LocalTime.of(7, 0)), timeArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(11, 0)), timeArgumentCaptor.getAllValues().get(1));
    assertEquals(email, stringArgumentCaptor.getAllValues().get(2));
  }

  @Test
  void get_Leads_returns_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class,sqlConnection.getLeads().getClass());
  }


  @Test
  void get_Leads_returns_all_lead_from_the_database() throws SQLException
  {
    Mockito.when(sqlConnection.getLeads()).thenReturn(new ArrayList<>(List.of(new Lead("Test1", null, "Lead", "le1@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available")
            ,new Lead("Test2", null, "Lead", "le2@gmail.com", "+4544554455","CFO", 7456, "BMW Motors", "Available"))));

    ArrayList<Object> leads=sqlConnection.getLeads();
    Mockito.verify(connection).prepareStatement("select * from lead");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,leads.size());

    Lead lead1= (Lead) leads.get(0);
    assertEquals(lead1.getFirstname(),"Test1");
    assertEquals(lead1.getMiddleName(),null);
    assertEquals(lead1.getLastname(),"Lead");
    assertEquals(lead1.getEmail(),"le1@gmail.com");
    assertEquals(lead1.getPhone(),"+4555445544");
    assertEquals(lead1.getTitle(),"CEO");
    assertEquals(lead1.getBusiness_id(),7456);
    assertEquals(lead1.getBusinessName(),"BMW Motors");
    assertEquals(lead1.getStatus(),"Available");
    Lead lead2= (Lead) leads.get(1);
    assertEquals(lead2.getFirstname(),"Test2");
    assertEquals(lead2.getMiddleName(),null);
    assertEquals(lead2.getLastname(),"Lead");
    assertEquals(lead2.getEmail(),"le2@gmail.com");
    assertEquals(lead2.getPhone(),"+4544554455");
    assertEquals(lead2.getTitle(),"CFO");
    assertEquals(lead2.getBusiness_id(),7456);
    assertEquals(lead2.getBusinessName(),"BMW Motors");
    assertEquals(lead2.getStatus(),"Available");
  }

  @Test
  void getting_BusinessName_by_id_will_return_the_exact_BusinessName_from_database()
      throws SQLException
  {
    int id=1234;
    Mockito.when(sqlConnection.getBusinessNameByID(id)).thenReturn("Test1");

    String name=sqlConnection.getBusinessNameByID(id);
    System.out.println(name);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(1),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();

    assertEquals(name,"Test1");
    assertEquals(stringArgumentCaptor.getValue(),"select businessname from business where business_id = ?");
    assertEquals(integerArgumentCaptor.getValue(), id);
  }

  @Test
  void getting_BusinessName_by_id_returns_a_string() throws SQLException
  {
    assertEquals(sqlConnection.getBusinessNameByID(1234).getClass(),String.class);
  }


  @Test
  void getAttendance_returns_an_arraylist() throws SQLException
  {
    Meeting meeting= new Meeting("Meeting", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), null);
    Mockito.when(sqlConnection.getAttendance(meeting)).thenReturn(new ArrayList<>(List.of("e1@gmail.com", "e2@gmail.com","e3@gmail.com")));
    assertEquals(ArrayList.class,sqlConnection.getAttendance(meeting).getClass());
  }
  @Test
  void getting_Attendance_based_on_meeting_gives_back_user_emails()
      throws SQLException
  {
    Meeting meeting= new Meeting("Meeting", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), null);
    Mockito.when(sqlConnection.getAttendance(meeting)).thenReturn(new ArrayList<>(List.of("e1@gmail.com", "e2@gmail.com","e3@gmail.com")));

    ArrayList<String>emails=sqlConnection.getAttendance(meeting);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(2), dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(3), timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(4), timeArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    assertEquals(3, emails.size());
    assertEquals("select * from attendance"
        + " where title = ? and date = ? and starttime = ? and endtime = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Meeting",stringArgumentCaptor.getAllValues().get(1));
    assertEquals(Date.valueOf(LocalDate.of( 2023, 5, 22)),dateArgumentCaptor.getValue());
    assertEquals(Time.valueOf(LocalTime.of(7, 0)),timeArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(11, 0)),timeArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void removing_the_attendances_of_a_meeting_will_delete_them_from_the_attendance_table()
      throws SQLException
  {
    Meeting meeting = new Meeting("Meeting", "",
        Date.valueOf(LocalDate.of(2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11, 0)),
        null);
    sqlConnection.removeAttendance(meeting);

    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(2), dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(3), timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(4), timeArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("delete from attendance where" + " title = ? and date = ? and starttime = ? and endtime = ? ",
        stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Meeting", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(Date.valueOf(LocalDate.of(2023, 5, 22)), dateArgumentCaptor.getValue());
    assertEquals(Time.valueOf(LocalTime.of(7, 0)),
        timeArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(11, 0)),
        timeArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void creating_new_lead_will_update_lead_table() throws SQLException
  {
    Lead lead=new Lead("Test", null, "Lead", "le1@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available");
    sqlConnection.createLead(lead);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(7),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("INSERT INTO Lead(firstName, middleName, lastName, email, phone, title, business_id) VALUES (?, ?, ?, ?, ?, ?, ?)",stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Test",stringArgumentCaptor.getAllValues().get(1));
    assertEquals(null,stringArgumentCaptor.getAllValues().get(2));
    assertEquals("Lead",stringArgumentCaptor.getAllValues().get(3));
    assertEquals("le1@gmail.com",stringArgumentCaptor.getAllValues().get(4));
    assertEquals("+4555445544",stringArgumentCaptor.getAllValues().get(5));
    assertEquals("CEO",stringArgumentCaptor.getAllValues().get(6));
    assertEquals(7456, integerArgumentCaptor.getValue());
  }

  @Test
  void creating_new_user_will_update_user_table() throws SQLException
  {

    User user=new User("Test", null, "User", "user@gmail.com", "+4544556677",false,"Street 1", 8700);
    String password="password";
    sqlConnection.createUser(user,password);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6),stringArgumentCaptor.capture());
    Mockito.verify(statement).setBoolean(eq(7),booleanArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(8),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(9),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals(stringArgumentCaptor.getAllValues().get(0), "INSERT Into \"user\"(firstname, middlename, lastname, email, password, phone, role, street, postalcode) VALUES (?,?,?,?,?,?,?,?,?)");
    assertEquals(stringArgumentCaptor.getAllValues().get(1),"Test");
    assertEquals(stringArgumentCaptor.getAllValues().get(2),null);
    assertEquals(stringArgumentCaptor.getAllValues().get(3),"User");
    assertEquals(stringArgumentCaptor.getAllValues().get(4),"user@gmail.com");
    assertEquals(stringArgumentCaptor.getAllValues().get(5),"password");
    assertEquals(stringArgumentCaptor.getAllValues().get(6),"+4544556677");
    assertEquals(booleanArgumentCaptor.getValue(),false);
    assertEquals(stringArgumentCaptor.getAllValues().get(7),"Street 1");
    assertEquals(integerArgumentCaptor.getValue(),8700);
  }

  @Test
  void creating_new_address_will_update_address_table() throws SQLException
  {
    Address address=new Address("Street 1", "Horsens", "Denmark", 8700);
    sqlConnection.createAddress(address);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(4),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("insert into address(street,city,country,postalcode) values(?,?,?,?)", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Street 1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("Horsens", stringArgumentCaptor.getAllValues().get(2));
    assertEquals("Denmark", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(8700, integerArgumentCaptor.getValue());
  }

  @Test
  void getting_address_returns_an_Address() throws SQLException
  {
    Address address=new Address("Street 1", "Horsens", "Denmark", 8700);
    Mockito.when(sqlConnection.getAddress(address)).thenReturn(address);
    assertEquals(Address.class, sqlConnection.getAddress(address).getClass());
  }

  @Test
  void getting_an_address_by_address_will_return_an_address_from_the_database()
      throws SQLException
  {
    Address address=new Address("Street 1", "Horsens", "Denmark", 8700);
    Mockito.when(sqlConnection.getAddress(address)).thenReturn(address);
    Address result=sqlConnection.getAddress(address);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(4),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    assertEquals(stringArgumentCaptor.getAllValues().get(0),"select * from address where street = ? and city = ? and country = ? and postalcode = ?");
    assertEquals(result.getStreet(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals(result.getCity(),stringArgumentCaptor.getAllValues().get(2));
    assertEquals(result.getCountry(),stringArgumentCaptor.getAllValues().get(3));
    assertEquals(result.getPostalCode(),integerArgumentCaptor.getValue());
  }

  @Test
  void creating_a_business_will_update_business_table() throws SQLException
  {
    Business business=new Business("Test1", "Street 1",1122);
    sqlConnection.createBusiness(business);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2), stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(3), integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();
    assertEquals("insert into business(businessname, street, postalcode) values (?,?,?)",stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Test1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("Street 1", stringArgumentCaptor.getAllValues().get(2));
    assertEquals(1122, integerArgumentCaptor.getValue());
  }

  @Test
  void getting_Business_id_returns_an_int() throws SQLException
  {
    Business business=new Business("Test1", "Street 1",1122);
    Mockito.when(sqlConnection.getBusinessID(business)).thenReturn(1122);
    int id=sqlConnection.getBusinessID(business);
    assertEquals(1122, id);
  }

  @Test
  void getBusinessID_gives_back_the_id_of_the_business_in_database()
      throws SQLException
  {
    Business business=new Business("Test", "Street 1",1122);
    Mockito.when(sqlConnection.getBusinessID(business)).thenReturn(business.getBusiness_id());
    int result=sqlConnection.getBusinessID(business);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2), stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(3), integerArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    assertEquals(stringArgumentCaptor.getAllValues().get(0),"select business_id from business where businessname = ? and street = ? and postalcode = ?");
    assertEquals(stringArgumentCaptor.getAllValues().get(1),"Test");
    assertEquals(stringArgumentCaptor.getAllValues().get(2),"Street 1");
    assertEquals(integerArgumentCaptor.getValue(),1122);
    assertEquals(result,business.getBusiness_id());
  }

  @Test
  void editLead_updates_the_Lead_table() throws SQLException
  {
    Lead oldLead=new Lead("Test", null, "Lead", "le1@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available");
    Lead newLead=new Lead("Test", null, "Lead", "le@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available");
    sqlConnection.editLead(oldLead,newLead);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(7),integerArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(8), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(9), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(10), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(11), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(12), stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(13), stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(14),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("update lead set firstname = ?, middlename = ?, lastname = ?,"
        + "email = ?, phone = ?, title = ?, business_id = ? where firstname = ? and middlename = ? and lastname = ? and "
        + "email = ? and phone = ? and title = ? and business_id = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Test", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(null, stringArgumentCaptor.getAllValues().get(2));
    assertEquals("Lead", stringArgumentCaptor.getAllValues().get(3));
    assertEquals("le@gmail.com", stringArgumentCaptor.getAllValues().get(4));
    assertEquals("+4555445544", stringArgumentCaptor.getAllValues().get(5));
    assertEquals("CEO", stringArgumentCaptor.getAllValues().get(6));
    assertEquals(7456, integerArgumentCaptor.getAllValues().get(0));
    assertEquals("Test", stringArgumentCaptor.getAllValues().get(7));
    assertEquals(null, stringArgumentCaptor.getAllValues().get(8));
    assertEquals("Lead", stringArgumentCaptor.getAllValues().get(9));
    assertEquals("le1@gmail.com", stringArgumentCaptor.getAllValues().get(10));
    assertEquals("+4555445544", stringArgumentCaptor.getAllValues().get(11));
    assertEquals("CEO", stringArgumentCaptor.getAllValues().get(12));
    assertEquals(7456, integerArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void getMeetingsByLead_returns_an_arraylist() throws SQLException
  {
    Lead lead=new Lead("Test", null, "Lead", "le1@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available");
    assertEquals(ArrayList.class,sqlConnection.getMeetingsByLead(lead).getClass());
  }

  @Test
  void getting_meetings_by_lead_gives_back_all_the_correct_meetings_wit_the_lead_from_database()
      throws SQLException
  {
    Lead lead=new Lead("Test", null, "Lead", "le1@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available");
    Mockito.when(sqlConnection.getMeetingsByLead(lead)).thenReturn(new ArrayList<>(
        List.of(new Meeting("Meeting1", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
                Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com"),
            new Meeting("Meeting2", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
                Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com"))
    ));

    ArrayList<Meeting> meetings=sqlConnection.getMeetingsByLead(lead);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    assertEquals(2,meetings.size());
    assertEquals("select * from meeting where email = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals("le1@gmail.com",stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void removing_a_meeting_will_update_the_Meeting_table() throws SQLException
  {
    Meeting meeting = new Meeting("Meeting", "",
        Date.valueOf(LocalDate.of(2023, 5, 22)),
        Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11, 0)),
        null);
    sqlConnection.removeMeeting(meeting);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setDate(eq(2),dateArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(3),timeArgumentCaptor.capture());
    Mockito.verify(statement).setTime(eq(4),timeArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("delete from meeting where title = ? and date = ? and starttime = ? and endtime = ? and email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Meeting", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(Date.valueOf(LocalDate.of(2023, 5, 22)), dateArgumentCaptor.getValue());
    assertEquals(Time.valueOf(LocalTime.of(7, 0)),timeArgumentCaptor.getAllValues().get(0));
    assertEquals(Time.valueOf(LocalTime.of(11, 0)),timeArgumentCaptor.getAllValues().get(1));
    assertEquals(null, stringArgumentCaptor.getAllValues().get(2));
  }

  @Test
  void removing_a_lead_will_update_the_Lead_table() throws SQLException
  {
    Lead lead=new Lead("Test", null, "Lead", "le1@gmail.com", "+4555445544","CEO", 7456, "BMW Motors", "Available");
    sqlConnection.removeLead(lead);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(6), integerArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("delete from lead where firstname = ? and lastname = ? and email = ? and phone = ? and title = ? and business_id = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("Test", stringArgumentCaptor.getAllValues().get(1));
    assertEquals("Lead", stringArgumentCaptor.getAllValues().get(2));
    assertEquals("le1@gmail.com", stringArgumentCaptor.getAllValues().get(3));
    assertEquals("+4555445544", stringArgumentCaptor.getAllValues().get(4));
    assertEquals("CEO", stringArgumentCaptor.getAllValues().get(5));
    assertEquals(7456,integerArgumentCaptor.getValue());
  }

  @Test
  void getting_user_by_email_returns_a_User() throws SQLException
  {
    String email="testuser@gmail.com";
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getUserByEmail(email)).thenReturn(user);
    assertEquals(User.class,sqlConnection.getUserByEmail(email).getClass());
  }

  @Test
  void getting_user_by_email_gives_back_the_user_with_matching_email_from_database()
      throws SQLException
  {
    String email="testuser@gmail.com";
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getUserByEmail(email)).thenReturn(user);
    User result=sqlConnection.getUserByEmail(email);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    assertSame(user,result);
    assertEquals("SELECT * FROM \"user\" INNER JOIN address ON \"user\".street = address.street AND \"user\".postalcode = address.postalcode WHERE email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals(email,stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void logIn_will_return_a_boolean() throws SQLException
  {
    assertEquals(false,sqlConnection.logIn("",""));
  }
  @Test
  void logIn_checks_the_user_in_the_database() throws SQLException
  {
    String email= "email@gmail.com";
    String password="password";
    Mockito.when(resultSet.next()).thenReturn(true);
    Mockito.when(resultSet.getString("email")).thenReturn("email@gmail.com");
    Mockito.when(resultSet.getString("password")).thenReturn("password");
    sqlConnection.logIn(email,password);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    Mockito.verify(resultSet).getString("email");
    Mockito.verify(resultSet).getString("password");

    assertEquals("select * from \"user\" where email = ? and password = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals(email,stringArgumentCaptor.getAllValues().get(1));
    assertEquals(password,stringArgumentCaptor.getAllValues().get(2));
    assertTrue(sqlConnection.logIn(email,password));
  }

  @Test
  void getMeetingsByUser_returns_an_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class, sqlConnection.getMeetingsByUser(new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433)).getClass());
  }
  @Test
  void getMeetingsByUser_calls_the_database() throws SQLException
  {
    Mockito.when(resultSet.next()).thenReturn(true,true, false);
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getMeetingsByUser(user)).thenReturn(new ArrayList<>(List.of(new Meeting("Meeting1", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
            Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com"),
        new Meeting("Meeting2", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
            Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com"))));
    ArrayList<Object> meetings=sqlConnection.getMeetingsByUser(user);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    Mockito.verify(resultSet,Mockito.times(3)).next();
    Mockito.verify(resultSet,Mockito.times(2)).getString("title");
    Mockito.verify(resultSet,Mockito.times(2)).getString("description");
    Mockito.verify(resultSet,Mockito.times(2)).getDate("date");
    Mockito.verify(resultSet,Mockito.times(2)).getTime("starttime");
    Mockito.verify(resultSet,Mockito.times(2)).getTime("endtime");
    Mockito.verify(resultSet,Mockito.times(2)).getString("email");

    assertEquals("select * from meeting m join attendance a on m.title = a.title and m.date = a.date and m.starttime = a.starttime and m.endtime = a.endtime\n"
        + " where a.email = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals(user.getEmail(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals(meetings.size(),2);
  }

  @Test
  void getTasksByUser_returns_an_arraylist() throws SQLException
  {
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    assertEquals(ArrayList.class,sqlConnection.getTasksByUser(user).getClass());
  }

  @Test
  void getTasksByUser_calls_the_database() throws SQLException
  {
    Mockito.when(resultSet.next()).thenReturn(true,true,false);
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getTasksByUser(user)).thenReturn(new ArrayList<>(List.of(new Task("Task1","", Date.valueOf(LocalDate.of( 2023, 5, 22)),"To do", 7456),new Task("Task2","",Date.valueOf(LocalDate.of( 2023, 5, 24)),"Ready", 7456))));
    ArrayList<Object> tasks=sqlConnection.getTasksByUser(user);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    Mockito.verify(resultSet,Mockito.times(3)).next();
    Mockito.verify(resultSet,Mockito.times(2)).getString("title");
    Mockito.verify(resultSet,Mockito.times(2)).getString("description");
    Mockito.verify(resultSet,Mockito.times(2)).getDate("dueDate");
    Mockito.verify(resultSet,Mockito.times(2)).getString("status");
    Mockito.verify(resultSet,Mockito.times(2)).getInt("business_id");

    assertEquals("select * from task t join assignment a on t.title = a.title and t.duedate = a.duedate and t.business_id = a.business_id\n"
        + "where a.email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals(user.getEmail(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals(2,tasks.size());
  }

  @Test
  void getUserPassword_returns_a_string() throws SQLException
  {
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getUserPassword(user.getEmail())).thenReturn("password");
    assertEquals(String.class, sqlConnection.getUserPassword(user.getEmail()).getClass());
  }
  @Test
  void getUserPassword_returns_null_if_user_does_not_exist_in_database() throws SQLException
  {
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    assertEquals(null,sqlConnection.getUserPassword(user.getEmail()));
  }

  @Test
  void getUserPassword_calls_the_database() throws SQLException
  {
    Mockito.when(resultSet.next()).thenReturn(true);
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getUserPassword(user.getEmail())).thenReturn("password");
    String password=sqlConnection.getUserPassword(user.getEmail());
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    Mockito.verify(resultSet).next();
    Mockito.verify(resultSet).getString("password");

    assertEquals("select password from \"user\" where email = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals(user.getEmail(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals("password",password);
  }

  @Test
  void removeUser_removes_existing_user() throws SQLException
  {
    sqlConnection.removeUser("email@gmail.com");
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("email@gmail.com",stringArgumentCaptor.getAllValues().get(1));
    assertEquals("delete from \"user\" where email = ?", stringArgumentCaptor.getAllValues().get(0));
  }

  @Test
  void removeAssignmentsForUser_removes_all_assignments_from_database_for_the_given_user()
      throws SQLException
  {
    sqlConnection.removeAssignmentsForUser("email@gmail.com");
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("delete from assignment where email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("email@gmail.com",stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void removeAttendanceForUser_removes_all_attendances_from_database_for_the_given_user()
      throws SQLException
  {
    sqlConnection.removeAttendanceForUser("email@gmail.com");
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("delete from attendance where email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals("email@gmail.com",stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  void editUser_updates_the_database() throws SQLException
  {
    User userOld=new User("Test1", null,"User", "testuser1@gmail.com","+4511221122",false,"Street 1",4433);
    User userNew=new User("Test1", null,"User", "testuser2@gmail.com","+4533443344",false,"Street 2",4433);
    String password="password";

    sqlConnection.editUser(userOld,userNew,password);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(4),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(5),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(6),stringArgumentCaptor.capture());
    Mockito.verify(statement).setBoolean(eq(7),booleanArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(8),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(9),integerArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(10),stringArgumentCaptor.capture());
    Mockito.verify(statement).executeUpdate();

    assertEquals("update \"user\" set firstname = ?, middlename = ?, lastname = ?, email = ?, password = ?, phone = ?, role = ?, street = ?, postalcode = ? where "
        + "email = ?", stringArgumentCaptor.getAllValues().get(0));
    assertEquals(userNew.getFirstName(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals(userNew.getMiddleName(),stringArgumentCaptor.getAllValues().get(2));
    assertEquals(userNew.getLastName(),stringArgumentCaptor.getAllValues().get(3));
    assertEquals(userNew.getEmail(),stringArgumentCaptor.getAllValues().get(4));
    assertEquals(password,stringArgumentCaptor.getAllValues().get(5));
    assertEquals(userNew.getPhone(),stringArgumentCaptor.getAllValues().get(6));
    assertEquals(userNew.isManager(),booleanArgumentCaptor.getValue());
    assertEquals(userNew.getStreet(),stringArgumentCaptor.getAllValues().get(7));
    assertEquals(userNew.getPostalCode(),integerArgumentCaptor.getValue());
    assertEquals(userOld.getEmail(),stringArgumentCaptor.getAllValues().get(8));
  }

  @Test
  void getAddress_returns_an_Object() throws SQLException
  {
    Mockito.when(sqlConnection.getAddress(new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433)
)).thenReturn(new Object());
    assertEquals(Object.class,sqlConnection.getAddress(new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433)).getClass());
  }
  @Test
  void getAddress_gets_the_address_of_the_given_user_from_the_database()
      throws SQLException
  {
    Mockito.when(resultSet.next()).thenReturn(true);
    User user=new User("Test1", null,"User", "testuser@gmail.com","+4511223344",false,"Street 1",4433);
    Mockito.when(sqlConnection.getAddress(user)).thenReturn(new Address("Street 1","Horsens","Denmark",8700));
    Object address=sqlConnection.getAddress(user);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(2),integerArgumentCaptor.capture());
    Mockito.verify(statement).executeQuery();
    Mockito.verify(resultSet).next();
    Mockito.verify(resultSet).getString("city");
    Mockito.verify(resultSet).getString("country");

    assertEquals(new Address("Street 1","Horsens","Denmark",8700),address);
    assertEquals("select * from address where street = ? and postalcode = ?",stringArgumentCaptor.getAllValues().get(0));
    assertEquals(user.getStreet(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals(user.getPostalCode(),integerArgumentCaptor.getValue());
  }

  @Test
  void removeAddress_removes_the_given_address_from_the_database()
      throws SQLException
  {
    Mockito.when(resultSet.next()).thenReturn(true);
    Address address=new Address("Street 1","Horsens","Denmark",8700);
    sqlConnection.removeAddress(address);
    Mockito.verify(connection).prepareStatement(stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(1),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(2),stringArgumentCaptor.capture());
    Mockito.verify(statement).setString(eq(3),stringArgumentCaptor.capture());
    Mockito.verify(statement).setInt(eq(4),integerArgumentCaptor.capture());

    assertEquals("delete from address where street = ? and city = ? and country = ? and postalcode = ? ",stringArgumentCaptor.getAllValues().get(0));
    assertEquals(address.getStreet(),stringArgumentCaptor.getAllValues().get(1));
    assertEquals(address.getCity(),stringArgumentCaptor.getAllValues().get(2));
    assertEquals(address.getCountry(),stringArgumentCaptor.getAllValues().get(3));
    assertEquals(address.getPostalCode(),integerArgumentCaptor.getValue());


  }

}
