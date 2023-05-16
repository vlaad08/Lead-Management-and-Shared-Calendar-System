package app.model.listeners;

import app.model.Model;
import app.shared.User;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class UserListener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<User>
{

  private final Model model;

  public UserListener(Model model) throws RemoteException
  {
    this.model = model;
  }

  @Override public void propertyChange(
      RemotePropertyChangeEvent<User> remotePropertyChangeEvent)
      throws RemoteException
  {
    if(remotePropertyChangeEvent.getPropertyName().equals("reloadUsers"))
    {
      try
      {
        model.reloadUsers();
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}
