package app.model.listeners;


import app.model.Model;
import app.shared.Meeting;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class MessageListener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<Meeting>
{
  private final ReloadData model;

  public MessageListener(ReloadData model) throws RemoteException{
    this.model = model;

  }

  @Override public void propertyChange(
      RemotePropertyChangeEvent<Meeting> remotePropertyChangeEvent)
      throws RemoteException
  {
    if(remotePropertyChangeEvent.getPropertyName().equals("reloadMeeting"))
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
