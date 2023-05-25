package app.server;

import app.JDBC.SQLConnection;
import app.JDBC.SQLConnectionTest;
import app.shared.*;
import app.view.LoginController;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;
import org.mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestServerImplementation
{


  private SQLConnection sqlConnection;
  private ServerImplementation serverImplementation;
  private RemotePropertyChangeSupport<String> support;

  private Field supportField;

  @BeforeEach
  void setUp()
      throws NoSuchFieldException, IllegalAccessException, SQLException
  {
    serverImplementation = Mockito.spy(ServerImplementation.class);
    sqlConnection = Mockito.mock(SQLConnection.class);

    support = Mockito.mock(RemotePropertyChangeSupport.class);

    serverImplementation.setSupport(support);
    serverImplementation.setConnection(sqlConnection);

    supportField = serverImplementation.getClass().getDeclaredField("support");
    supportField.setAccessible(true);
    supportField.set(serverImplementation, support);



  }

  @Test
  void server_implementation_is_created() {
    assertNotNull(supportField);
  }

  @Test
  void listener_is_added() throws RemoteException {
    RemotePropertyChangeListener<String> listener = Mockito.mock(RemotePropertyChangeListener.class);


    serverImplementation.addListener(listener);


    Mockito.verify(serverImplementation).addListener(listener);
    Mockito.verify(support).addPropertyChangeListener(listener);
  }

  @Test
  void create_address_if_not_exists() throws SQLException, RemoteException {
    Address address = new Address("DummyStreet", "DummyCity", "DummyCountry", 123);

    Mockito.when(sqlConnection.getAddress(address)).thenReturn(null);


    serverImplementation.addObject(address);

    assertNotSame(address,sqlConnection.getAddress(address));

    Mockito.verify(sqlConnection).createAddress(address);
    Mockito.verify(support).firePropertyChange("reloadAddress", null, "");

  }

  @Test void no_address_created_if_it_exists() throws SQLException, RemoteException
  {
    Address address = new Address("DummyStreet", "DummyCity", "DummyCountry", 123);

    Mockito.when(sqlConnection.getAddress(address)).thenReturn(address);

    serverImplementation.addObject(address);
    assertSame(address,sqlConnection.getAddress(address));
    Mockito.verify(sqlConnection, Mockito.never()).createAddress(address);
    Mockito.verify(support, Mockito.never()).firePropertyChange("reloadAddress", null, "");
  }


  @Test void create_business() throws SQLException, RemoteException
  {
     Business business = new Business("DummyBusiness", "DummyStreet", 123);

     serverImplementation.addObject(business);

     Mockito.verify(sqlConnection).createBusiness(business);
     Mockito.verify(support).firePropertyChange("reloadBusiness", null, "");

  }

  @Test
  void create_lead() throws SQLException, RemoteException {
    Lead lead = new Lead("DummyLead", "", "", "dummylead@gmail.com", "", "", 0, "", "");
    serverImplementation.addObject(lead);

    Mockito.verify(sqlConnection).createLead(lead);
    Mockito.verify(support).firePropertyChange("reloadLead", null, "");
  }

  @Test void create_user() throws SQLException, RemoteException
  {
    User user = new User("DummyUser", "", "", "email@gmail.com", "", false, "", 0);
    String password = "password";

    serverImplementation.addObjectWithPassword(user, password);

    Mockito.verify(sqlConnection).createUser(user, password);
    Mockito.verify(support).firePropertyChange("reloadUser", null, "");

  }

  @Test void create_meeting() throws SQLException, RemoteException
  {
    Meeting meeting = new Meeting("DummyMeeting", "",
        Date.valueOf(LocalDate.of(2023, 6, 10)),
        Time.valueOf(LocalTime.of(10, 30)), Time.valueOf(LocalTime.of(11, 30)),
        "leademail@gmai.com");

    ArrayList<String> emails = new ArrayList<>(List.of(
        "user1@gmail.com", "user2@gmail.com"
    ));

    serverImplementation.addObject(meeting, emails);

    Mockito.verify(sqlConnection).createMeeting(meeting);

    for(String e : emails)
    {
      Mockito.verify(sqlConnection).setAttendance(e, meeting);
    }

    Mockito.verify(support).firePropertyChange("reloadMeeting", null, "");
  }


  @Test void create_task() throws SQLException, RemoteException
  {
    Task task = new Task("DummyTask", "", Date.valueOf(LocalDate.of(2023, 6, 10)),
        "To do", 0);
    ArrayList<String> emails = new ArrayList<>(List.of(
        "user1@gmail.com", "user2@gmail.com"
    ));

    serverImplementation.addObject(task, emails);

    Mockito.verify(sqlConnection).createTask(task);

    for(String e : emails)
    {
      Mockito.verify(sqlConnection).assignTask(task,e);
    }

    Mockito.verify(support).firePropertyChange("reloadTask", null, "");

  }

  @Test void get_list_returns_empty_list() throws SQLException, RemoteException
  {
    assertEquals(0, serverImplementation.getList("").size());
  }

  @Test void get_list_of_meetings() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Date date2 = Date.valueOf(LocalDate.of(2023, 6, 11));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));

    Mockito.when(sqlConnection.getMeetings()).thenReturn(
        new ArrayList<>(
            List.of(
                new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee"),
                new Meeting("meeting2", "", date2, time1, time2, "e@e.ee")
            )
        )
    );

    serverImplementation.getList("meetings");


    ArrayList<Object> list = sqlConnection.getMeetings();
    ArrayList<Meeting> meetings = new ArrayList<>();
    for(Object obj : list)
    {
      meetings.add((Meeting) obj);
    }

    assertEquals("meeting1", meetings.get(0).getTitle());
    assertEquals("meeting2", meetings.get(1).getTitle());

  }

  @Test void get_list_of_tasks() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Date date2 = Date.valueOf(LocalDate.of(2023, 6, 11));

    Mockito.when(sqlConnection.getTasks()).thenReturn(
        new ArrayList<>(List.of(
            new Task("task1", "", date1, "To do", 0),
            new Task("task2", "", date2, "To do", 0)
        ))
    );


    serverImplementation.getList("tasks");

    ArrayList<Object> list = sqlConnection.getTasks();
    ArrayList<Task> tasks = new ArrayList<>();
    for(Object obj : list)
    {
      tasks.add((Task) obj);
    }

    assertEquals("task1", tasks.get(0).getTitle());
    assertEquals("task2", tasks.get(1).getTitle());
  }

  @Test void get_list_of_users() throws SQLException, RemoteException
  {
    Mockito.when(sqlConnection.getUsers()).thenReturn(
        new ArrayList<>(List.of(
            new User("user1", "", "", "", "", true, "", 12),
            new User("user2", "", "", "", "", false, "", 12)
        ))
    );

    serverImplementation.getList("users");

    ArrayList<Object> list = sqlConnection.getUsers();
    ArrayList<User> users = new ArrayList<>();
    for(Object obj : list)
    {
      users.add((User) obj);
    }

    assertEquals("user1", users.get(0).getFirstName());
    assertEquals("user2", users.get(1).getFirstName());
  }

  @Test void get_list_of_leads() throws SQLException, RemoteException
  {
    Mockito.when(sqlConnection.getLeads()).thenReturn(
        new ArrayList<>(List.of(
            new Lead("lead1", "", "", "", "", "", 0, "", ""),
            new Lead("lead2", "", "", "", "", "", 0, "", "")
        ))
    );

    serverImplementation.getList("users");


    ArrayList<Object> list = sqlConnection.getLeads();
    ArrayList<Lead> leads = new ArrayList<>();
    for(Object obj : list)
    {
      leads.add((Lead) obj);
    }

    assertEquals("lead1", leads.get(0).getFirstname());
    assertEquals("lead2", leads.get(1).getFirstname());
  }

  @Test void get_list_of_businesses() throws SQLException, RemoteException
  {
    Mockito.when(sqlConnection.getBusinesses()).thenReturn(
        new ArrayList<>(List.of(
            new Business("business1", "", 0),
            new Business("business2", "", 0)
        ))
    );

    serverImplementation.getList("businesses");


    ArrayList<Object> list = sqlConnection.getBusinesses();
    ArrayList<Business> businesses = new ArrayList<>();
    for(Object obj : list)
    {
      businesses.add((Business) obj);
    }

    assertEquals("business1", businesses.get(0).getName());
    assertEquals("business2", businesses.get(1).getName());
  }

  @Test void no_list_of_employees_is_returned()
      throws SQLException, RemoteException
  {
    assertEquals(0, serverImplementation.getListOfEmployees(null).size());
  }

  @Test void get_list_of_employees_email_by_meeting() throws SQLException
  {
    ArrayList<String> employees = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));
    Meeting meeting = new Meeting("DummyMeeting", "",
        Date.valueOf(LocalDate.of(2023, 6, 10)),
        Time.valueOf(LocalTime.of(10, 30)), Time.valueOf(LocalTime.of(11, 30)),
        "leademail@gmai.com");

    Mockito.when(sqlConnection.getAttendance(meeting)).thenReturn(employees);

    ArrayList<String> returnedString = sqlConnection.getAttendance(meeting);

    assertEquals("employee1@gmail.com", returnedString.get(0));
    assertEquals("employee2@gmail.com", returnedString.get(1));
  }

  @Test void get_list_of_employees_email_by_task() throws SQLException
  {
    ArrayList<String> employees = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));
    Task task = new Task("DummyTask", "", Date.valueOf(LocalDate.of(2023, 6, 10)),
        "To do", 0);

    Mockito.when(sqlConnection.getAssignedUsers(task)).thenReturn(employees);

    ArrayList<String> returnedString = sqlConnection.getAssignedUsers(task);

    assertEquals("employee1@gmail.com", returnedString.get(0));
    assertEquals("employee2@gmail.com", returnedString.get(1));
  }

  @Test void create_attendance_for_meting_and_get_list_of_available_employees_in_time_period()
      throws SQLException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));

    ArrayList<Object> meetings = new ArrayList<>(
        List.of(
            new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee"),
            new Meeting("meeting2", "", date1, time1, time2, "e@e.ee")
        )
    );
    ArrayList<String> users = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));


    Mockito.when(sqlConnection.getMeetings()).thenReturn(meetings);





    assertNotNull(meetings);
    assertNotNull(users);
  }
}
