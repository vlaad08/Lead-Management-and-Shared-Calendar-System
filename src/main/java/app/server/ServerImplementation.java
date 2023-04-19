package app.server;

import app.model.Lead;
import app.model.Meeting;
import app.model.Task;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

public class ServerImplementation implements Server
{
  private RemotePropertyChangeSupport<String> support;

  public ServerImplementation(){
    support = new RemotePropertyChangeSupport<>();
  }

  @Override
  public void addPropertyChangeListener(RemotePropertyChangeListener<String> listener) throws RemoteException
  {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void addMeeting(Meeting meeting) throws RemoteException
  {

  }

  @Override public void manageMeeting(Meeting meeting) throws RemoteException
  {

  }

  @Override public void addTask(Task task) throws RemoteException
  {

  }

  @Override public void manageTask(Task task) throws RemoteException
  {

  }

  @Override public void addLead(Lead lead) throws RemoteException
  {

  }

  @Override public void manageLead(Lead lead) throws RemoteException
  {

  }
}
