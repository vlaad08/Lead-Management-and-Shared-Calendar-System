package app.server;

import app.shared.Lead;
import app.shared.Meeting;
import app.shared.Task;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public interface Server extends Remote
{
  void addPropertyChangeListener(RemotePropertyChangeListener<String> listener) throws RemoteException;

  public void addMeeting(Meeting meeting)throws RemoteException;
  public void manageMeeting(Meeting deletedMeeting, Meeting createdMeeting) throws RemoteException;
  public void removeMeeting(Meeting meeting)throws RemoteException;
  public ArrayList<Meeting> getMeetings()throws RemoteException;
  void addTask(Task task);
  ArrayList<Task> getTasks() throws RemoteException;

  //We don't need the code below right know

  /*
  public void manageTask(Task deletedTask, Task createdTask) throws RemoteException;
  public void removeTask(Task task)throws RemoteException;
  public void addLead(Lead lead)throws RemoteException;
  public void manageLead(Lead deletedLead, Lead createdLead)throws RemoteException;
  public void removeLead(Lead lead) throws RemoteException;
  public ArrayList<Lead> getLeads()throws RemoteException;

   */

}
