package app.model.listeners;

import app.model.Model;
import app.shared.Task;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class TaskListener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<Task>
{
  public Model model;

  public TaskListener(Model model) throws RemoteException
  {
    this.model = model;
  }

  @Override public void propertyChange(
      RemotePropertyChangeEvent<Task> remotePropertyChangeEvent)
      throws RemoteException
  {
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
  }
}
