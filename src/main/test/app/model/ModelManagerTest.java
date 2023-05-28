package app.model;

import app.shared.*;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class ModelManagerTest
{
  private ModelManager model;
  private Communicator communicator;
  private PropertyChangeSupport support;
  private User loggedInUser;

  @BeforeEach
  void setUp()
  {
    loggedInUser=Mockito.mock(User.class);
    support=Mockito.mock(PropertyChangeSupport.class);
    communicator=Mockito.mock(Communicator.class);
    model=new ModelManager(communicator);
    model.setSupport(support);
  }

  @Test
  void communicator_is_set_up_in_constructor()
      throws NoSuchFieldException, IllegalAccessException
  {
    Field communicatorField = ModelManager.class.getDeclaredField("communicator");
    communicatorField.setAccessible(true);
    Communicator actualCommunicator = (Communicator) communicatorField.get(model);

    assertNotNull(actualCommunicator);
  }
  @Test
  void PropertyChangeSupport_is_set_up_in_constructor()
      throws NoSuchFieldException, IllegalAccessException
  {
    Field supportField = ModelManager.class.getDeclaredField("support");
    supportField.setAccessible(true);
    PropertyChangeSupport actualSupport = (PropertyChangeSupport) supportField.get(model);

    assertNotNull(actualSupport);
  }

  @Test
  void communicator_addObject_is_called_during_addObject()
      throws SQLException, RemoteException
  {
    model.addObject(new Object());
    Mockito.verify(communicator).addObject(Mockito.any());
  }
  @Test
  void communicator_addObject_is_called_during_addObject_with_emails()
      throws SQLException, RemoteException
  {
    Object obj=new Object();
    ArrayList<String> emails=new ArrayList<>(List.of("e1@gmail.com","e2@gmail.com","e3@gmail.com"));
    model.addObject(obj,emails);
    Mockito.verify(communicator).addObject(obj,emails);
  }
  @Test
  void addObject_recognizes_SQLException() throws SQLException, RemoteException
  {
    Mockito.doThrow(SQLException.class).when(communicator).addObject(Mockito.any());
    assertThrows(SQLException.class,()-> model.addObject(new Object()));
  }
  @Test
  void addObject_with_emails_recognizes_SQLException() throws SQLException, RemoteException
  {
    Object obj=new Object();
    ArrayList<String> emails=new ArrayList<>(List.of("e1@gmail.com","e2@gmail.com","e3@gmail.com"));
    Mockito.doThrow(SQLException.class).when(communicator).addObject(obj,emails);
    assertThrows(SQLException.class,()-> model.addObject(obj,emails));
  }
  @Test
  void addObject_recognizes_RemoteException() throws SQLException, RemoteException
  {
    Mockito.doThrow(RemoteException.class).when(communicator).addObject(Mockito.any());
    assertThrows(RemoteException.class,()-> model.addObject(new Object()));
  }
  @Test
  void addObject_with_emails_recognizes_RemoteException() throws SQLException, RemoteException
  {
    Object obj=new Object();
    ArrayList<String> emails=new ArrayList<>(List.of("e1@gmail.com","e2@gmail.com","e3@gmail.com"));
    Mockito.doThrow(RemoteException.class).when(communicator).addObject(obj,emails);
    assertThrows(RemoteException.class,()-> model.addObject(obj,emails));
  }

  @Test
  void getList_with_expectedType_returns_an_arraylist()
      throws SQLException, RemoteException
  {
    assertEquals(model.getList("meetings").getClass(),ArrayList.class);
    Mockito.verify(communicator).getList("meetings");
    assertEquals(model.getList("tasks").getClass(),ArrayList.class);
    Mockito.verify(communicator).getList("tasks");
    assertEquals(model.getList("users").getClass(),ArrayList.class);
    Mockito.verify(communicator).getList("users");
    assertEquals(model.getList("leads").getClass(),ArrayList.class);
    Mockito.verify(communicator).getList("leads");
    assertEquals(model.getList("businesses").getClass(),ArrayList.class);
    Mockito.verify(communicator).getList("businesses");
  }
  @Test
  void when_the_argument_is_not_recognised_by_getList_than_return_empty_ArrayList()
      throws SQLException, RemoteException
  {
    assertTrue(model.getList("other").isEmpty());
    Mockito.verify(communicator).getList("other");
  }
  @Test
  void communicator_will_not_return_an_arraylist_with_mismatched_argument()
      throws SQLException, RemoteException
  {
    model.getList("meetings");
    Mockito.verify(communicator).getList("meetings");
    Mockito.verify(communicator,Mockito.never()).getList("other");
    Mockito.verify(communicator,Mockito.never()).getList("asdasd");
  }

  @Test
  void getList_returns_all_meetings_or_tasks_from_database_when_user_is_manager()
      throws SQLException, RemoteException
  {
    model.getList("meetings");
    Mockito.verify(communicator).getList("meetings");
    model.getList("tasks");
    Mockito.verify(communicator).getList("tasks");
  }

  @Test
  void getList_returns_only_relevant_meetings_or_tasks_from_database_when_user_is_employee()
      throws SQLException, RemoteException, NoSuchFieldException
  {
    User employee = new User("", "", "", "", "", false, "", 1122);
    model.setLoggedInUser(employee);
    model.getList("meetings");
    Mockito.verify(communicator,Mockito.never()).getList("meetings");
    Mockito.verify(communicator).getListByUser(employee,"meetings");
    model.getList("tasks");
    Mockito.verify(communicator,Mockito.never()).getList("tasks");
    Mockito.verify(communicator).getListByUser(employee,"tasks");
  }

  @Test
  void getList_recognises_SQLException() throws SQLException, RemoteException {
    Mockito.when(communicator.getList("other")).thenThrow(SQLException.class);
    assertThrows(RuntimeException.class, () -> {
      model.getList("other");
    });
  }

  @Test
  void getList_recognises_RemoteException() throws SQLException, RemoteException
  {
    Mockito.when(communicator.getList("other")).thenThrow(RemoteException.class);
    assertThrows(RuntimeException.class,()->{
      model.getList("other");
    });
  }

  @Test
  void getting_list_by_object_returns_an_arraylist()
  {
    assertEquals(model.getList(new Object()).getClass(),ArrayList.class);
  }

  @Test
  void getting_list_by_object_returns_the_list_of_Employees_for_given_meeting_or_task()
      throws SQLException, RemoteException
  {
    Meeting meeting=new Meeting("Meeting","", Date.valueOf(LocalDate.of(2023,5,10)),
        Time.valueOf(LocalTime.of(9,0)),Time.valueOf(LocalTime.of(11,0)),"email@gmail.com");
    Task task= new Task("Task","",Date.valueOf(LocalDate.of(2023,5,11)), "To do",1234);
    model.getList(meeting);
    model.getList(task);
    Mockito.verify(communicator).getListOfEmployees(meeting);
    Mockito.verify(communicator).getListOfEmployees(task);
  }

  @Test
  void getting_list_by_meeting_recognizes_SQLException()
      throws SQLException, RemoteException
  {
    Mockito.when(communicator.getListOfEmployees(Mockito.any())).thenThrow(
        SQLException.class);
    Meeting meeting=new Meeting("Meeting","", Date.valueOf(LocalDate.of(2023,5,10)),
        Time.valueOf(LocalTime.of(9,0)),Time.valueOf(LocalTime.of(11,0)),"email@gmail.com");
    assertThrows(RuntimeException.class, ()->{
      model.getList(meeting);
    });
  }
  @Test
  void getting_list_by_meeting_recognizes_RemoteException()
      throws SQLException, RemoteException
  {
    Mockito.when(communicator.getListOfEmployees(Mockito.any())).thenThrow(
        RemoteException.class);
    Meeting meeting=new Meeting("Meeting","", Date.valueOf(LocalDate.of(2023,5,10)),
        Time.valueOf(LocalTime.of(9,0)),Time.valueOf(LocalTime.of(11,0)),"email@gmail.com");
    assertThrows(RuntimeException.class, ()->{
      model.getList(meeting);
    });
  }
  @Test
  void getting_list_by_task_recognizes_SQLException()
      throws SQLException, RemoteException
  {
    Mockito.when(communicator.getListOfEmployees(Mockito.any())).thenThrow(
        SQLException.class);
    Task task= new Task("Task","",Date.valueOf(LocalDate.of(2023,5,11)), "To do",1234);
    assertThrows(RuntimeException.class, ()->{
      model.getList(task);
    });
  }
  @Test
  void getting_list_by_task_recognizes_RemoteException()
    throws SQLException, RemoteException
  {
    Mockito.when(communicator.getListOfEmployees(Mockito.any())).thenThrow(
        RemoteException.class);
    Task task= new Task("Task","",Date.valueOf(LocalDate.of(2023,5,11)), "To do",1234);
    assertThrows(RuntimeException.class, ()->{
      model.getList(task);
    });
  }

  @Test
  void getting_list_by_obj_returns_arraylist()
      throws SQLException, RemoteException
  {
    Task task= new Task("Task","",Date.valueOf(LocalDate.of(2023,5,11)), "To do",1234);
    assertEquals(ArrayList.class, model.getList(task).getClass());
  }
  @Test
  void get_list_returns_an_array_of_users_if_object_is_meeting()
      throws SQLException, RemoteException
  {
    Meeting meeting = new Meeting("", "", new Date(2030, 2, 2), new Time(3000),
        new Time(300000), "doesntmatter@gmail.com");

    model.getList(meeting);
    Mockito.verify(communicator).getListOfEmployees(meeting);
  }

  @Test
  void getting_list_by_obj_recognizes_SQLException()
      throws SQLException, RemoteException
  {
    Mockito.when(communicator.getListOfEmployees(Mockito.any())).thenThrow(
        SQLException.class);
    assertThrows(RuntimeException.class,()->{
      model.getList(new Object());
    });
  }
  @Test
  void getting_list_by_obj_recognizes_RemoteException()
      throws SQLException, RemoteException
  {
    Mockito.when(communicator.getListOfEmployees(Mockito.any())).thenThrow(SQLException.class);
    assertThrows(RuntimeException.class, () -> {
      model.getList(new Object());
    });
  }

  @Test
  void getting_list_by_date_and_times_returns_an_arraylist()
      throws SQLException, RemoteException
  {
    assertEquals(ArrayList.class,model.getList(Date.valueOf(LocalDate.of(2023,5,12)), Time.valueOf(LocalTime.of(11,0)),Time.valueOf(LocalTime.of(13,0))).getClass());
  }
  @Test
  void getting_list_by_date_and_times_will_return_the_list_of_available_employees()
      throws SQLException, RemoteException
  {
    model.getList(Date.valueOf(LocalDate.of(2023,5,12)), Time.valueOf(LocalTime.of(11,0)),Time.valueOf(LocalTime.of(13,0)));
    Mockito.verify(communicator).getListOfAvailableEmployees(Mockito.any(),Mockito.any(),Mockito.any());
  }

  @Test
  void removing_an_obj_calls_the_communicator()
      throws SQLException, RemoteException
  {
    model.removeObject(new Object());
    Mockito.verify(communicator).removeObject(Mockito.any());
  }

  @Test
  void edit_object_calls_the_communicator() throws SQLException, RemoteException
  {
    model.editObject(new Object(),new Object());
    Mockito.verify(communicator).editObject(Mockito.any(),Mockito.any());
  }

  @Test
  void edit_object_with_list_edits_attendance_based_on_object()
      throws SQLException, RemoteException
  {
    Meeting meeting1 = new Meeting("", "", new Date(2030, 2, 2), new Time(3000), new Time(300000), "lead@gmail.com");
    Meeting meeting2 = new Meeting("", "", new Date(2030, 2, 2), new Time(3000), new Time(300000), "lead1234@gmail.com");
    ArrayList<String> emails=new ArrayList<>(List.of("e1@gmail.com","e2@gmail.com","e3@gmail.com"));
    model.editObjectWithList(meeting1,meeting2,emails);
    Mockito.verify(communicator).editObjectWithList(meeting1,meeting2,emails);
  }
  @Test
  void edit_object_with_list_edits_assignment_based_on_object()
      throws SQLException, RemoteException
  {
    Task task1= new Task("TaskOld","",Date.valueOf(LocalDate.of(2023,5,11)), "To do",1234);
    Task task2= new Task("TaskNew","",Date.valueOf(LocalDate.of(2023,5,11)), "To do",1234);
    ArrayList<String> emails=new ArrayList<>(List.of("e1@gmail.com","e2@gmail.com","e3@gmail.com"));
    model.editObjectWithList(task1,task2,emails);
    Mockito.verify(communicator).editObjectWithList(task1,task2,emails);
  }

  @Test
  void add_property_change_listener_calls_support()
  {
    PropertyChangeListener listener=Mockito.mock(PropertyChangeListener.class);
    model.addPropertyChangeListener(listener);
    Mockito.verify(support).addPropertyChangeListener(Mockito.any());
  }

  @Test
  void getBusinessId_calls_the_communicator()
      throws SQLException, RemoteException
  {
    model.getBusinessId(new Business("Test Business", "Street 2", 3344));
    Mockito.verify(communicator).getBusinessId(Mockito.any());
  }

  @Test
  void login_calls_communicator() throws SQLException, RemoteException
  {
    model.logIn("email@gmail.com","pass");
    Mockito.verify(communicator).logIn(Mockito.any(),Mockito.any());
  }
  @Test
  void login_returns_a_boolean() throws SQLException, RemoteException
  {
    Mockito.when(communicator.logIn(Mockito.any(),Mockito.anyString())).thenReturn(true);
    assertTrue(model.logIn("email@gmail.com","pass"));
  }
  @Test
  void if_login_fails_false_is_returned() throws SQLException, RemoteException
  {
    Mockito.when(communicator.logIn(Mockito.any(),Mockito.any())).thenReturn(false);
    assertFalse(model.logIn("email@gmail.com","pass"));
  }
  @Test
  void if_login_succeeded_user_is_saved_and_reloads_fire()
      throws SQLException, RemoteException
  {
    Mockito.when(communicator.logIn(Mockito.any(),Mockito.any())).thenReturn(true);
    model.setSupport(support);
    model.logIn("email@gmail.com","pass");
    Mockito.verify(communicator).getUserByEmail(Mockito.any());
    Mockito.verify(support).firePropertyChange("reloadMeetings",false,true);
    Mockito.verify(support).firePropertyChange("reloadTasks",false,true);
    Mockito.verify(support).firePropertyChange("reloadLoggedInUser",false,true);
  }

  @Test
  void getUserPassword_calls_the_communicator()
      throws SQLException, RemoteException
  {
    model.getUserPassword("oldemail@gmail.com");
    Mockito.verify(communicator).getUserPassword(Mockito.any());
  }

  @Test
  void addObjectWithPassword_calls_hte_communicator()
      throws SQLException, RemoteException
  {
    model.addObjectWithPassword(new User("Test", "","User", "email@gmail.com","+45554455",false,"Street 1",1122),"pass");
    Mockito.verify(communicator).addObjectWithPassword(Mockito.any(),Mockito.any());
  }

  @Test
  void editObjectWithPassword_calls_the_communicator()
      throws SQLException, RemoteException
  {
    model.editObjectWithPassword(new Object(),new Object(),"");
    Mockito.verify(communicator).editObjectWithPassword(Mockito.any(),Mockito.any(),Mockito.any());
  }
  @Test
  void getObject_calls_the_communicator() throws SQLException, RemoteException
  {
    model.getObject(new Object(),"");
    Mockito.verify(communicator).getObject(Mockito.any(),Mockito.any());
  }

  @Test
  void meetingAddedFromServer_fires_a_PropertyChange()
  {
    model.meetingAddedFromServer();
    Mockito.verify(support,Mockito.times(1)).firePropertyChange("reloadMeetings",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadLeads",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadUser",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadTasks",false,true);
  }
  @Test
  void leadAddedFromServer_fires_a_PropertyChange()
  {
    model.leadAddedFromServer();
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadMeetings",false,true);
    Mockito.verify(support,Mockito.times(1)).firePropertyChange("reloadLeads",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadUser",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadTasks",false,true);
  }
  @Test
  void userAddedFromServer_fires_a_PropertyChange()
  {
    model.userAddedFromServer();
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadMeetings",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadLeads",false,true);
    Mockito.verify(support,Mockito.times(1)).firePropertyChange("reloadUser",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadTasks",false,true);
  }
  @Test
  void taskAddedFromServer_fires_a_PropertyChange()
  {
    model.taskAddedFromServer();
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadMeetings",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadLeads",false,true);
    Mockito.verify(support,Mockito.never()).firePropertyChange("reloadUser",false,true);
    Mockito.verify(support,Mockito.times(1)).firePropertyChange("reloadTasks",false,true);
  }

  @Test
  void getLoggedInUser_gets_back_the_user()
  {
    model.setLoggedInUser(loggedInUser);
    assertSame(loggedInUser,model.getLoggedInUser());
  }
}
