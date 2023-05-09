package app.server;

import app.JDBC.SQLConnection;
import app.shared.Lead;
import app.shared.Meeting;
import app.shared.Task;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerImplementation implements Server
{
  private RemotePropertyChangeSupport<Meeting> supportMeeting;
  private RemotePropertyChangeSupport<Task> supportTask;
  private RemotePropertyChangeSupport<Lead> supportLead;
  private ArrayList<Meeting> meetingList;
  private ArrayList<Task> taskList;
  private ArrayList<Lead> leadList;

  private SQLConnection connection;

  public ServerImplementation(){
    meetingList = new ArrayList<>();
    taskList = new ArrayList<>();
    leadList = new ArrayList<>();
    supportMeeting = new RemotePropertyChangeSupport<>();
    try{
      this.connection = SQLConnection.getInstance();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  @Override public void addPropertyChangeListener(
      RemotePropertyChangeListener<Meeting> meetingListener)
      throws RemoteException
  {
    supportMeeting.addPropertyChangeListener(meetingListener);
  }

  @Override
  public void addMeeting(Meeting meeting) throws RemoteException
  {
    meetingList.add(meeting);
    supportMeeting.firePropertyChange("meeting",null,meeting);
  }

  @Override public void manageMeeting(Meeting dealitedMeeting, Meeting createdMeeting) throws RemoteException
  {
    meetingList.remove(dealitedMeeting);
    meetingList.add(createdMeeting);
    supportMeeting.firePropertyChange("meeting",null,createdMeeting);
  }

  @Override public void removeMeeting(Meeting meeting) throws RemoteException
  {
    meetingList.remove(meeting);
    supportMeeting.firePropertyChange("meeting",null,meeting);
  }

  @Override
  public ArrayList<Meeting> getMeetings() throws RemoteException
  {
    return meetingList;
  }

  //We don't need the code that is bellow right know.
  /*
  @Override public void addTask(Task task) throws RemoteException
  {
    taskList.add(task);
    supportTask.firePropertyChange("task",null,task);
  }

  @Override public void manageTask(Task deletedTask, Task createdTask) throws RemoteException
  {
    taskList.remove(deletedTask);
    taskList.add(createdTask);
    supportTask.firePropertyChange("task",null,createdTask);
  }

  @Override public void removeTask(Task task) throws RemoteException
  {
    taskList.remove(task);
    supportTask.firePropertyChange("task",null,task);
  }

  @Override public ArrayList<Task> getTasks() throws RemoteException
  {
    return taskList;
  }

  @Override public void addLead(Lead lead) throws RemoteException
  {
    leadList.add(lead);
    supportLead.firePropertyChange("lead",null,lead);
  }

  @Override public void manageLead(Lead deletedLead, Lead createdLead) throws RemoteException
  {
    leadList.remove(deletedLead);
    leadList.add(createdLead);
    supportLead.firePropertyChange("lead",null,createdLead);
  }

  @Override public void removeLead(Lead lead) throws RemoteException
  {
    leadList.remove(lead);
    supportLead.firePropertyChange("lead",null,lead);
  }

  @Override public ArrayList<Lead> getLeads() throws RemoteException
  {
    return leadList;
  }

   */
}
