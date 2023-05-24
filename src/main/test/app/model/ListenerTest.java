package app.model;

import dk.via.remote.observer.RemotePropertyChangeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
  void propertyChange_calls_model_meetingAddedFromServer_when_propertyName_is_reloadMeeting() throws RemoteException,
      SQLException
  {
    RemotePropertyChangeEvent<String> event = new RemotePropertyChangeEvent<>("reloadMeeting", "old","new");
    listener.propertyChange(event);

    Mockito.verify(model, Mockito.times(1)).meetingAddedFromServer();
    Mockito.verify(model, Mockito.never()).taskAddedFromServer();
    Mockito.verify(model, Mockito.never()).leadAddedFromServer();
    Mockito.verify(model, Mockito.never()).userAddedFromServer();
    Mockito.verify(model, Mockito.never()).businessAddedFromServer();
  }
}
