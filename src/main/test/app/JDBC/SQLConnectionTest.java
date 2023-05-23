package app.JDBC;

import app.shared.Business;
import app.shared.Meeting;
import app.shared.Task;
import app.shared.User;
import org.mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

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
    ArrayList<Meeting> meetings = sqlConnection.getMeetings();
    Mockito.verify(connection).prepareStatement("select * from meeting");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,meetings.size());

    Meeting meeting1 = meetings.get(0);
    assertEquals(meeting1.getTitle(),"Meeting1");
    assertEquals(meeting1.getDescription(),"");
    assertEquals(meeting1.getDate(),Date.valueOf(LocalDate.of( 2023, 5, 22)));
    assertEquals(meeting1.getStartTime(),Time.valueOf(LocalTime.of(7, 0)));
    assertEquals(meeting1.getEndTime(),Time.valueOf(LocalTime.of(11,0)));
    assertEquals(meeting1.getLeadEmail(),"le1@gmail.com");
    Meeting meeting2 = meetings.get(1);
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

    ArrayList<Task> tasks = sqlConnection.getTasks();
    Mockito.verify(connection).prepareStatement("select * from task");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,tasks.size());

    Task task1 = tasks.get(0);
    assertEquals(task1.getTitle(),"Task1");
    assertEquals(task1.getDescription(),"");
    assertEquals(task1.getDate(),Date.valueOf(LocalDate.of( 2023, 5, 22)));
    assertEquals(task1.getStatus(),"To do");
    assertEquals(task1.getBusiness_id(),7456);
    Task task2 = tasks.get(1);
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
    Mockito.when(sqlConnection.getUsers()).thenReturn(new ArrayList<>(List.of(new User("Test1", null,"User", "testuser1@gmail.com","+4511223344",false,"Street 1",4433, "TestCity", "TestCountry"),new User("Test2", null,"User", "testuser2@gmail.com","+4522334455",true,"Street 2",4433, "TestCity", "TestCountry"))));

    ArrayList<User> users=sqlConnection.getUsers();
    Mockito.verify(connection).prepareStatement("SELECT * FROM \"user\" INNER JOIN address ON \"user\".street = address.street AND \"user\".postalcode = address.postalcode");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,users.size());

    User user1=users.get(0);
    assertEquals(user1.getFirstName(),"Test1");
    assertEquals(user1.getMiddleName(),null);
    assertEquals(user1.getLastName(),"User");
    assertEquals(user1.getEmail(),"testuser1@gmail.com");
    assertEquals(user1.getPhone(),"+4511223344");
    assertEquals(user1.isManager(),false);
    assertEquals(user1.getStreet(),"Street 1");
    assertEquals(user1.getPostalCode(),4433);
    assertEquals(user1.getCity(),"TestCity");
    assertEquals(user1.getCountry(),"TestCountry");

    User user2=users.get(1);
    assertEquals(user2.getFirstName(),"Test2");
    assertEquals(user2.getMiddleName(),null);
    assertEquals(user2.getLastName(),"User");
    assertEquals(user2.getEmail(),"testuser2@gmail.com");
    assertEquals(user2.getPhone(),"+4522334455");
    assertEquals(user2.isManager(),true);
    assertEquals(user2.getStreet(),"Street 2");
    assertEquals(user2.getPostalCode(),4433);
    assertEquals(user2.getCity(),"TestCity");
    assertEquals(user2.getCountry(),"TestCountry");
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

    ArrayList<Business> businesses=sqlConnection.getBusinesses();
    Mockito.verify(connection).prepareStatement("select * from business");
    Mockito.verify(statement).executeQuery();
    assertEquals(2,businesses.size());

    Business business1=businesses.get(0);
    assertEquals(business1.getName(), "Test1");
    assertEquals(business1.getStreet(), "Street 1");
    assertEquals(business1.getPostalCode(), 1122);
    Business business2=businesses.get(1);
    assertEquals(business2.getName(), "Test2");
    assertEquals(business2.getStreet(), "Street 2");
    assertEquals(business2.getPostalCode(), 1122);
  }

  @Test
  void assign_Task_changes_the_assignment_table()
  {

  }
}
