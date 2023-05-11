package app.model;

import app.shared.Communicator;

import app.shared.Meeting;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class MessageListener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<Meeting>
{
  private Model model;

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
        Meeting meeting = remotePropertyChangeEvent.getNewValue();
        model.meetingAddedFromServer(meeting);
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}
