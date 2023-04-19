package app.server;

import app.model.Lead;
import app.model.Meeting;
import app.model.Task;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote
{
  void addPropertyChangeListener(RemotePropertyChangeListener<String> listener) throws
      RemoteException;

  public void addMeeting(Meeting meeting)throws RemoteException;
  public void manageMeeting(Meeting meeting) throws RemoteException;
  public void addTask(Task task) throws RemoteException;
  public void manageTask(Task task) throws RemoteException;
  public void addLead(Lead lead)throws RemoteException;
  public void manageLead(Lead lead)throws RemoteException;

}
