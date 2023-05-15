package app.model;


import app.shared.Meeting;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class MessageListener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<Meeting>
{
  private final Model model;

  public MessageListener(Model model) throws RemoteException{
    this.model = model;

  }

  @Override public void propertyChange(
      RemotePropertyChangeEvent<Meeting> remotePropertyChangeEvent)
      throws RemoteException
  {
    if(remotePropertyChangeEvent.getPropertyName().equals("Meeting Created"))
    {
      try
      {
        model.meetingAddedFromServer();
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}
