package app.server;

import app.JDBC.SQLConnection;
import app.shared.*;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;
import org.junit.Assert;
import org.mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.plugins.MemberAccessor;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
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
      throws NoSuchFieldException, IllegalAccessException
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


      Mockito.verify(sqlConnection,Mockito.times(2)).setAttendance(ArgumentMatchers.anyString(), ArgumentMatchers.eq(meeting));


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




    ArrayList<Object> list = serverImplementation.getList("meetings");
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




    ArrayList<Object> list = serverImplementation.getList("tasks");
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



    ArrayList<Object> list = serverImplementation.getList("users");
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




    ArrayList<Object> list = serverImplementation.getList("leads");;
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




    ArrayList<Object> list = serverImplementation.getList("businesses");
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

  @Test void get_list_of_employees_email_by_meeting()
      throws SQLException, RemoteException
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

    ArrayList<String> returnedString = serverImplementation.getListOfEmployees(meeting);

    assertEquals("employee1@gmail.com", returnedString.get(0));
    assertEquals("employee2@gmail.com", returnedString.get(1));
  }

  @Test void get_list_of_employees_email_by_task()
      throws SQLException, RemoteException
  {
    ArrayList<String> employees = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));
    Task task = new Task("DummyTask", "", Date.valueOf(LocalDate.of(2023, 6, 10)),
        "To do", 0);



    Mockito.when(sqlConnection.getAssignedUsers(task)).thenReturn(employees);

    ArrayList<String> returnedString = serverImplementation.getListOfEmployees(task);

    assertEquals("employee1@gmail.com", returnedString.get(0));
    assertEquals("employee2@gmail.com", returnedString.get(1));
  }

  @Test void create_attendance_for_meting_and_get_list_of_available_employees_in_time_period()
      throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    Meeting meeting1 = new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee");




    ArrayList<String> emails = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));

    Mockito.when(sqlConnection.getUsers()).thenReturn(
        new ArrayList<>(
            List.of(
                new User("user1", "", "", "employee1@gmail.com", "", false, "", 0),
                new User("user2", "", "", "employee2@gmail.com", "", false, "", 0)
            )
        )
    );

    Mockito.when(sqlConnection.getUserByEmail("employee1@gmail.com")).thenReturn(
        new User("user1", "", "", "employee1@gmail.com", "", false, "", 0));

    Mockito.when(sqlConnection.getAttendance(Mockito.any())).thenReturn(new ArrayList<>(
        List.of(
            "employee1@gmail.com"
        )
    ));
    Mockito.when(sqlConnection.getMeetings()).thenReturn(
        new ArrayList<>(
            List.of(
                new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee"),
                new Meeting("meeting2", "", date1, time3, time4, "e@e.ee")
            )
        )
    );


    sqlConnection.setAttendance(emails.get(0), meeting1);

    ArrayList<Object> list = serverImplementation.getListOfAvailableEmployees(date1, time3, time4);

    ArrayList<User> users = new ArrayList<>();

    for(Object obj : list)
    {
      users.add((User) obj);
    }

    assertEquals("employee2@gmail.com", users.get(0).getEmail());
    assertNotNull(emails);
  }


  @Test void get_empty_list_of_available_users_in_time_period()
      throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    Meeting meeting1 = new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee");

    Meeting meeting2 = new Meeting("meeting2", "", date1, time1, time2, "a@a.ee");


    ArrayList<String> emails = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));

    Mockito.when(sqlConnection.getUsers()).thenReturn(
        new ArrayList<>(
            List.of(
                new User("user1", "", "", "employee1@gmail.com", "", false, "", 0),
                new User("user2", "", "", "employee2@gmail.com", "", false, "", 0)
            )
        )
    );

    Mockito.when(sqlConnection.getUserByEmail("employee1@gmail.com")).thenReturn(
        new User("user1", "", "", "employee1@gmail.com", "", false, "", 0));

    Mockito.when(sqlConnection.getUserByEmail("employee2@gmail.com")).thenReturn(
        new User("user2", "", "", "employee2@gmail.com", "", false, "", 0));


    Mockito.when(sqlConnection.getAttendance(Mockito.any())).thenReturn(new ArrayList<>(
        List.of(
            "employee1@gmail.com", "employee2@gmail.com"
        )
    ));
    Mockito.when(sqlConnection.getMeetings()).thenReturn(
        new ArrayList<>(
            List.of(
                new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee"),
                new Meeting("meeting2", "", date1, time3, time4, "a@a.ee")
            )
        )
    );


    sqlConnection.setAttendance(emails.get(0), meeting1);
    sqlConnection.setAttendance(emails.get(1), meeting2);

    ArrayList<Object> list = serverImplementation.getListOfAvailableEmployees(date1, time3, time4);


    assertNotNull(list);
    assertEquals(0, list.size());
    assertNotNull(emails);
  }

  @Test void get_list_of_meetings_by_user() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    User user = new User("user1", "", "", "employee1@gmail.com", "", false, "", 0);

    Mockito.when(sqlConnection.getMeetingsByUser(user)).thenReturn(
        new ArrayList<>(List.of(
            new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee"),
            new Meeting("meeting2", "", date1, time3, time4, "a@a.ee")
        ))
    );



    ArrayList<Object> list = serverImplementation.getListByUser(user, "meetings");
    ArrayList<Meeting> meetings = new ArrayList<>();
    meetings.add(0, (Meeting) list.get(0));
    meetings.add(1, (Meeting) list.get(1));


    Mockito.verify(sqlConnection).getMeetingsByUser(user);
    assertNotNull(list);
    assertEquals(2, meetings.size());
    assertEquals("meeting1", meetings.get(0).getTitle());
    assertEquals("meeting2", meetings.get(1).getTitle());
  }

  @Test void get_list_of_tasks_by_user() throws SQLException, RemoteException
  {
    Date  date1 = Date.valueOf(LocalDate.of(2023, 5, 10));
    Date  date2 = Date.valueOf(LocalDate.of(2023, 5, 15));
    User user = new User("user1", "", "", "employee1@gmail.com", "", false, "", 0);


    Mockito.when(sqlConnection.getTasksByUser(user)).thenReturn(
        new ArrayList<>(List.of(
            new Task("task1", "", date1, "To do", 0),
            new Task("task2", "", date2, "To do", 0)
        )
    ));

    ArrayList<Object> list = serverImplementation.getListByUser(user, "tasks");
    ArrayList<Task> tasks= new ArrayList<>();
    tasks.add(0, (Task) list.get(0));
    tasks.add(1, (Task) list.get(1));

    Mockito.verify(sqlConnection).getTasksByUser(user);
    assertNotNull(list);
    assertEquals(2, tasks.size());
    assertEquals("task1", tasks.get(0).getTitle());
    assertEquals("task2", tasks.get(1).getTitle());
  }

  @Test void get_empty_list_of_object_for_wrong_expected_type_inserted()
      throws SQLException, RemoteException
  {
    User user = new User("user1", "", "", "employee1@gmail.com", "", false, "", 0);


    ArrayList<Object> list = serverImplementation.getListByUser(user, "");

    Mockito.verify(sqlConnection, Mockito.never()).getMeetingsByUser(user);
    Mockito.verify(sqlConnection, Mockito.never()).getTasksByUser(user);
    assertNotNull(list);
    assertEquals(0, list.size());
  }


  @Test void edit_lead_and_compare_it_to_old_object()
      throws SQLException, RemoteException
  {

    Lead oldLead = new Lead("oldLead", "", "", "oldLead@gmail.com", "", "", 0, "", "");
    Lead newLead = new Lead("newLead", "", "", "newLead@gmail.com", "", "", 0, "", "");
    Date  date1 = Date.valueOf(LocalDate.of(2023, 5, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    Mockito.when(sqlConnection.getMeetingsByLead(oldLead)).thenReturn(
        new ArrayList<>(List.of(
            new Meeting("meeting1",  "", date1, time1, time2, "oldLead@gmail.com"),
            new Meeting("meeting2", "", date1, time3, time4, "oldLead@gmail.com")
        ))
    );

    Mockito.when(serverImplementation.getListOfEmployees(Mockito.any(Meeting.class))).thenReturn(
        new ArrayList<>(List.of("employee1@gmail.com"))
    );


    serverImplementation.editObject(oldLead,newLead);
    Mockito.verify(sqlConnection, Mockito.times(2)).removeAttendance(Mockito.any(Meeting.class));
    Mockito.verify(sqlConnection, Mockito.times(2)).removeMeeting(Mockito.any(Meeting.class));
    Mockito.verify(sqlConnection, Mockito.times(1)).editLead(oldLead,newLead);
    Mockito.verify(sqlConnection, Mockito.times(2)).createMeeting(Mockito.any(Meeting.class));
    Mockito.verify(sqlConnection, Mockito.times(2)).setAttendance(ArgumentMatchers.anyString(), ArgumentMatchers.any(Meeting.class));
    Mockito.verify(support).firePropertyChange("reloadLead", null, "");
    Mockito.verify(support).firePropertyChange("reloadMeeting", null, "");
  }

  @Test void edit_meeting() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    Meeting oldMeeting = new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee");
    Meeting newMeeting = new Meeting("meeting2", "", date1, time3, time4, "a@a.ee");

    ArrayList<String> emails = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));

    serverImplementation.editObjectWithList(oldMeeting,newMeeting,emails);

    Mockito.verify(sqlConnection,Mockito.times(1)).removeAttendance(oldMeeting);
    Mockito.verify(sqlConnection, Mockito.times(1)).editMeeting(oldMeeting,newMeeting);
    Mockito.verify(sqlConnection,Mockito.times(2)).setAttendance(ArgumentMatchers.any(
        String.class), ArgumentMatchers.eq(newMeeting));
    Mockito.verify(support).firePropertyChange("reloadMeeting", null, "");
  }

  @Test void edit_task() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Date date2 = Date.valueOf(LocalDate.of(2023,6, 12));

    Task oldTask = new Task("task1", "", date1, "", 0);
    Task newTask = new Task("task2", "", date2, "", 0);
    ArrayList<String> emails = new ArrayList<>(List.of(
        "employee1@gmail.com",
        "employee2@gmail.com"
    ));

    serverImplementation.editObjectWithList(oldTask,newTask,emails);

    Mockito.verify(sqlConnection, Mockito.times(1)).removeAssignedUsers(oldTask);
    Mockito.verify(sqlConnection, Mockito.times(1)).editTask(newTask,oldTask);
    Mockito.verify(sqlConnection,Mockito.times(2)).assignTask(ArgumentMatchers.eq(newTask), ArgumentMatchers.any(String.class));
    Mockito.verify(support).firePropertyChange("reloadTask", null, "");
  }

  @Test void edit_user_and_its_password() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Date date2 = Date.valueOf(LocalDate.of(2023,6, 12));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    Task task1 = new Task("task1", "", date1, "", 0);
    Task task2 = new Task("task2", "", date2, "", 0);

    Meeting meeting1 = new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee");
    Meeting meeting2 = new Meeting("meeting2", "", date1, time3, time4, "a@a.ee");

    User oldUser = new User("oldUser", "", "", "oldUser@gmail.com", "", false, "", 0);
    User newUser = new User("newUser", "", "", "newUser@gmail.com", "", false, "", 0);

    String password = "password";

    Mockito.when(sqlConnection.getTasksByUser(oldUser)).thenReturn(
        new ArrayList<>(List.of(task1,task2))
    );
    Mockito.when(sqlConnection.getMeetingsByUser(oldUser)).thenReturn(
        new ArrayList<>(List.of(meeting1, meeting2))
    );


    serverImplementation.editObjectWithPassword(oldUser, newUser, password);

    Mockito.verify(sqlConnection,Mockito.times(1)).removeAssignmentsForUser(oldUser.getEmail());
    Mockito.verify(sqlConnection,Mockito.times(1)).removeAttendanceForUser(oldUser.getEmail());
    Mockito.verify(sqlConnection, Mockito.times(1)).editUser(oldUser,newUser,password);
    Mockito.verify(sqlConnection,Mockito.times(2)).assignTask(ArgumentMatchers.any(Task.class), ArgumentMatchers.eq(newUser.getEmail()));
    Mockito.verify(sqlConnection,Mockito.times(2)).setAttendance(ArgumentMatchers.eq(newUser.getEmail()), ArgumentMatchers.any(Meeting.class));
    Mockito.verify(support).firePropertyChange("reloadUser", null, "");
    Mockito.verify(support).firePropertyChange("reloadMeeting", null, "");
    Mockito.verify(support).firePropertyChange("reloadTask", null, "");
  }

  @Test void get_users_address() throws SQLException, RemoteException
  {
    User user = new User("user", "", "", "user@gmail.com", "", false, "", 0);

    Mockito.when(sqlConnection.getAddress(user)).thenReturn(new Address("street", "", "", 1));

    Object obj = serverImplementation.getObject(user,"address");

    Mockito.verify(sqlConnection).getAddress(user);
    assertNotNull(obj);
  }

  @Test void return_null_if_object_is_not_a_user()
      throws SQLException, RemoteException
  {
    assertNull(    serverImplementation.getObject(ArgumentMatchers.any(Object.class), ArgumentMatchers.anyString()));
  }


  @Test void remove_address() throws SQLException, RemoteException
  {
    Address address = new Address("street", "", "", 1);

    serverImplementation.removeObject(address);
    Mockito.verify(sqlConnection).removeAddress(address);
  }

  @Test void remove_user() throws SQLException, RemoteException
  {
    User user = new User("user", "", "", "user@gmail.com", "", false, "", 0);

    serverImplementation.removeObject(user.getEmail());
    Mockito.verify(sqlConnection).removeAssignmentsForUser(user.getEmail());
    Mockito.verify(sqlConnection).removeAttendanceForUser(user.getEmail());
    Mockito.verify(sqlConnection).removeUser(user.getEmail());
    Mockito.verify(support).firePropertyChange("reloadUser", null, "");
  }

  @Test void remove_meeting() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));

    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));

    Meeting meeting1 = new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee");

    serverImplementation.removeObject(meeting1);

    Mockito.verify(sqlConnection).removeAttendance(meeting1);
    Mockito.verify(sqlConnection).removeMeeting(meeting1);
    Mockito.verify(support).firePropertyChange("reloadMeeting", null, "");
  }

  @Test void remove_task() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));

    Task task1 = new Task("task1", "", date1, "", 0);

    serverImplementation.removeObject(task1);

    Mockito.verify(sqlConnection).removeAssignedUsers(task1);
    Mockito.verify(sqlConnection).removeTask(task1);
    Mockito.verify(support).firePropertyChange("reloadTask", null, "");
  }

  @Test void remove_lead() throws SQLException, RemoteException
  {
    Date date1 = Date.valueOf(LocalDate.of(2023,6, 10));
    Time time1 = Time.valueOf(LocalTime.of(9, 30));
    Time time2 = Time.valueOf(LocalTime.of(11,30));
    Time time3 = Time.valueOf(LocalTime.of(10, 30));
    Time time4 = Time.valueOf(LocalTime.of(11, 30));

    Lead lead = new Lead("lead", "", "", "lead@gmail.com", "", "", 0, "", "");


    Meeting meeting1 = new Meeting("meeting1",  "", date1, time1, time2, "e@e.ee");
    Meeting meeting2 = new Meeting("meeting2", "", date1, time3, time4, "a@a.ee");



    Mockito.when(sqlConnection.getMeetingsByLead(lead)).thenReturn(
        new ArrayList<>(List.of(meeting1, meeting2))
    );

    serverImplementation.removeObject(lead);

    Mockito.verify(sqlConnection,Mockito.times(2)).removeAttendance(ArgumentMatchers.any(Meeting.class));
    Mockito.verify(sqlConnection,Mockito.times(2)).removeMeeting(ArgumentMatchers.any(Meeting.class));
    Mockito.verify(sqlConnection).removeLead(lead);
    Mockito.verify(support).firePropertyChange("reloadLead", null, "");
    Mockito.verify(support).firePropertyChange("reloadMeeting", null, "");
  }

  @Test void get_user_password_from_database() throws SQLException, RemoteException
  {
    String email = "emailexample@gmail.com";
    Mockito.when(sqlConnection.getUserPassword(email)).thenReturn("password");

    String password =     serverImplementation.getUserPassword(email);

  Mockito.verify(sqlConnection).getUserPassword(email);

    assertNotNull(password);
  }

  @Test void get_business_id_from_database() throws SQLException, RemoteException
  {
    Business business = new Business("business", "", 12);

    Mockito.when(sqlConnection.getBusinessID(business)).thenReturn(Mockito.any(Integer.class));


    int id = serverImplementation.getBusinessId(business);

    Mockito.verify(sqlConnection).getBusinessID(business);
    assertTrue(id >= 0);
  }

  @Test void user_logged_in_if_credentials_are_correct()
      throws SQLException, RemoteException
  {
    String email = "emailexample@gmail.com";
    String password = "password";

    Mockito.when(sqlConnection.logIn(email,password)).thenReturn(true);

    assertTrue(serverImplementation.logIn(email,password));
    Mockito.verify(sqlConnection).logIn(email,password);
  }

  @Test void user_not_logged_in_if_credentials_are_incorrect()
      throws SQLException, RemoteException
  {
    String email = "emailexample@gmail.com";
    String password = "password";

    Mockito.when(sqlConnection.logIn(email,password)).thenReturn(false);
    assertFalse(serverImplementation.logIn(email,password));
    Mockito.verify(sqlConnection).logIn(email,password);
  }

  @Test void get_user_by_user_email() throws SQLException, RemoteException
  {
    String email = "emailexample@gmail.com";

    Mockito.when(sqlConnection.getUserByEmail(email)).thenReturn(
        new User("Employee", "", "", email, "", false, "", 2)
    );

    assertNotNull(serverImplementation.getUserByEmail(email));
    Mockito.verify(sqlConnection).getUserByEmail(email);
  }
}
