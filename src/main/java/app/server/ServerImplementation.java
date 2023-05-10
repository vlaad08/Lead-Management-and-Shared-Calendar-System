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
  private RemotePropertyChangeSupport<String> support;
  private ArrayList<Meeting> meetingList;

  private SQLConnection connection;

  public ServerImplementation(){
    meetingList = new ArrayList<>();
    support = new RemotePropertyChangeSupport<>();
    try{
      this.connection = SQLConnection.getInstance();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  @Override public void addPropertyChangeListener(
      RemotePropertyChangeListener<String> listener)
      throws RemoteException
  {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void addMeeting(Meeting meeting) throws RemoteException
  {
    try{
      connection.createMeeting(meeting.title(), meeting.description()
          ,meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
    }catch (SQLException e){
      e.printStackTrace();
    }

    meetingList.add(meeting);
    support.firePropertyChange("meeting",null,meeting.toString());
  }

  @Override public void manageMeeting(Meeting dealitedMeeting, Meeting createdMeeting) throws RemoteException
  {
    meetingList.remove(dealitedMeeting);
    meetingList.add(createdMeeting);
    support.firePropertyChange("meeting",null,createdMeeting.toString());
  }

  @Override public void removeMeeting(Meeting meeting) throws RemoteException
  {
    meetingList.remove(meeting);
    support.firePropertyChange("meeting",null,meeting.toString());
  }

  @Override
  public ArrayList<Meeting> getMeetings() throws RemoteException
  {
    try{
      return connection.getMeetings();
    }catch (SQLException e){
      e.printStackTrace();
    }
    return null;
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
