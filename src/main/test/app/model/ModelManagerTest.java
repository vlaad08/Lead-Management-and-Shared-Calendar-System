package app.model;

import app.shared.Communicator;
import app.shared.User;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.sql.SQLException;
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


}
