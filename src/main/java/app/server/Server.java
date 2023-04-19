package app.server;

import app.model.Lead;
import app.model.Meeting;
import app.model.Task;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Server extends Remote
{
  void addPropertyChangeListener(RemotePropertyChangeListener<Meeting> MeetingListener,
      RemotePropertyChangeListener<Task> taskListener,
      RemotePropertyChangeListener<Lead> leadListener)
      throws RemoteException;

  public void addMeeting(Meeting meeting)throws RemoteException;
  public void manageMeeting(Meeting deletedMeeting, Meeting createdMeeting) throws RemoteException;
  public void removeMeeting(Meeting meeting)throws RemoteException;
  public ArrayList<Meeting> getMeetings()throws RemoteException;

  public void addTask(Task task) throws RemoteException;
  public void manageTask(Task deletedTask, Task createdTask) throws RemoteException;
  public void removeTask(Task task)throws RemoteException;
  public ArrayList<Task> getTasks()throws RemoteException;
  public void addLead(Lead lead)throws RemoteException;
  public void manageLead(Lead deletedLead, Lead createdLead)throws RemoteException;
  public void removeLead(Lead lead) throws RemoteException;
  public ArrayList<Lead> getLeads()throws RemoteException;

}
