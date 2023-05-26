package app.model;

import app.shared.Communicator;
import app.shared.Meeting;
import app.shared.Task;
import app.shared.User;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

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
  private Model model;
  private Communicator communicator;

  @BeforeEach
  void setUp()
  {
    communicator=Mockito.mock(Communicator.class);
    model=new ModelManager(communicator);
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
    model.addObject(Mockito.any());
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
    assertThrows(SQLException.class,()-> model.addObject(Mockito.any()));
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
    assertThrows(RemoteException.class,()-> model.addObject(Mockito.any()));
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
    Mockito.verify(communicator,Mockito.times(1)).getList("other");
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
    ModelManager modelManager =new ModelManager(Mockito.mock(Communicator.class));
    User employee = new User("", "", "", "", "", false, "", 1122);
    Whitebox.setInternalState(modelManager, "loggedInUser", employee);
    modelManager.getList("meetings");
    Mockito.verify(communicator,Mockito.never()).getList("meetings");
    Mockito.verify(communicator).getListByUser(employee,"meetings");
    modelManager.getList("tasks");
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
  void get_list_returns_an_array_of_users_if_object_is_meeting()
      throws SQLException, RemoteException
  {
    Meeting meeting = new Meeting("", "", new Date(2030, 2, 2), new Time(3000),
        new Time(300000), "doesntmatter@gmail.com");

    model.getList(meeting);
    Mockito.verify(communicator).getListOfEmployees(meeting);


  }

}
