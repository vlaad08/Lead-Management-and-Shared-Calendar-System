package app.model;

import dk.via.remote.observer.RemotePropertyChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ListenerTest
{
  private Listener listener;
  private Model model;

  @BeforeEach
  void setUp() throws RemoteException
  {
    model= Mockito.mock(Model.class);
    listener=new Listener(model);
  }

  @Test
  void propertyChange_calls_meetingAddedFromServer_when_propertyName_is_reloadMeeting() throws RemoteException,
      SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadMeeting", "old","new");
    listener.propertyChange(event);

    Mockito.verify(model, Mockito.times(1)).meetingAddedFromServer();
    Mockito.verify(model, Mockito.never()).taskAddedFromServer();
    Mockito.verify(model, Mockito.never()).leadAddedFromServer();
    Mockito.verify(model, Mockito.never()).userAddedFromServer();
  }

  @Test
  void propertyChange_calls_taskAddedFromServer_when_propertyName_is_reloadTask() throws RemoteException,
      SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadTask", "old","new");
    listener.propertyChange(event);

    Mockito.verify(model, Mockito.never()).meetingAddedFromServer();
    Mockito.verify(model, Mockito.times(1)).taskAddedFromServer();;
    Mockito.verify(model, Mockito.never()).leadAddedFromServer();
    Mockito.verify(model, Mockito.never()).userAddedFromServer();
  }

  @Test
  void propertyChange_calls_leadAddedFromServer_when_propertyName_is_reloadLead() throws RemoteException,
      SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadLead", "old","new");
    listener.propertyChange(event);

    Mockito.verify(model, Mockito.never()).meetingAddedFromServer();
    Mockito.verify(model, Mockito.never()).taskAddedFromServer();
    Mockito.verify(model, Mockito.times(1)).leadAddedFromServer();
    Mockito.verify(model, Mockito.never()).userAddedFromServer();
  }

  @Test
  void propertyChange_calls_userAddedFromServer_when_propertyName_is_reloadUser() throws RemoteException,
      SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadUser", "old","new");
    listener.propertyChange(event);

    Mockito.verify(model, Mockito.never()).meetingAddedFromServer();
    Mockito.verify(model, Mockito.never()).taskAddedFromServer();;
    Mockito.verify(model, Mockito.never()).leadAddedFromServer();
    Mockito.verify(model, Mockito.times(1)).userAddedFromServer();
  }

  @Test
  void old_and_new_values_are_caught() throws RemoteException, SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadUser", "old","new");
    listener.propertyChange(event);
    Mockito.verify(model, Mockito.times(1)).userAddedFromServer();
    assertEquals("old", event.getOldValue());
    assertEquals("new", event.getNewValue());
  }

  @Test
  void NullPointerException_when_trying_to_get_a_value()
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadBusiness", null,"new");
    assertThrows(NullPointerException.class, ()->{
      listener.propertyChange(event);
      event.getOldValue().equals("old");
    });
  }

  @Test
  void if_constraint_or_connection_or_invalid_SQLstatement_happens_throw_RuntimeException_in_case_of_reloadUser()
      throws RemoteException, SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadUser", null,"new");
    Mockito.doThrow(SQLException.class).when(model).userAddedFromServer();
    assertThrows(RuntimeException.class,()->listener.propertyChange(event));
  }
  @Test
  void if_constraint_or_connection_or_invalid_SQLstatement_happens_throw_RuntimeException_in_case_of_reloadLead()
      throws RemoteException, SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadLead", null,"new");
    Mockito.doThrow(SQLException.class).when(model).leadAddedFromServer();
    assertThrows(RuntimeException.class,()->listener.propertyChange(event));
  }
  @Test
  void if_constraint_or_connection_or_invalid_SQLstatement_happens_throw_RuntimeException_in_case_of_reloadTask()
      throws RemoteException, SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadTask", null,"new");
    Mockito.doThrow(SQLException.class).when(model).taskAddedFromServer();
    assertThrows(RuntimeException.class,()->listener.propertyChange(event));
  }
  @Test
  void if_constraint_or_connection_or_invalid_SQLstatement_happens_throw_RuntimeException_in_case_of_reloadMeeting()
      throws RemoteException, SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadMeeting", null,"new");
    Mockito.doThrow(SQLException.class).when(model).meetingAddedFromServer();
    assertThrows(RuntimeException.class,()->listener.propertyChange(event));
  }
}
