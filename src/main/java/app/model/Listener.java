package app.model;

import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Listener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<String>
{

  private final Model model;

  public Listener(Model model) throws RemoteException
  {
    this.model = model;
  }

  @Override public void propertyChange(
      RemotePropertyChangeEvent<String> remotePropertyChangeEvent)
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
    if(remotePropertyChangeEvent.getPropertyName().equals("reloadTask"))
    {
      try
      {
        model.taskAddedFromServer();
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }
    if(remotePropertyChangeEvent.getPropertyName().equals("reloadLead"))
    {
      try
      {
        model.leadAddedFromServer();
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}
