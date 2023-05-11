package app.server;

import app.JDBC.SQLConnection;
import app.shared.Communicator;
import app.shared.Lead;
import app.shared.Meeting;

import app.shared.Task;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerImplementation implements Communicator
{
  private final RemotePropertyChangeSupport<Meeting> meetingSupport;

  private SQLConnection connection;

<<<<<<< Updated upstream
  public ServerImplementation(){
    meetingList = new ArrayList<>();
    support = new RemotePropertyChangeSupport<>();
    try{
      this.connection = SQLConnection.getInstance();
    }catch (SQLException e){
      e.printStackTrace();
    }
=======

  public ServerImplementation() throws SQLException
  {
    meetingSupport = new RemotePropertyChangeSupport<>();

>>>>>>> Stashed changes
  }




  @Override public void createMeeting(Meeting meeting) throws SQLException, RemoteException
  {
     connection = SQLConnection.getInstance();
    connection.createMeeting(meeting.title(), meeting.description()
        ,meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
    meetingSupport.firePropertyChange("Meeting Created", null, meeting);
  }

  @Override public void createTask(Task task) throws SQLException, RemoteException
  {
<<<<<<< Updated upstream
    try{
      connection.createMeeting(meeting.title(), meeting.description()
          ,meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
      System.out.println("----------------");
    }catch (SQLException e){
      e.printStackTrace();
    }

    meetingList.add(meeting);
    support.firePropertyChange("meeting",null,meeting.toString());
=======

>>>>>>> Stashed changes
  }

  @Override public void createLead(Lead lead) throws SQLException, RemoteException
  {

  }

  @Override public void removeMeeting(Meeting meeting) throws RemoteException
  {
  }

<<<<<<< Updated upstream
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
=======
  @Override public void removeTask(Task task) throws SQLException
  {

  }

  @Override public void removeLead(Lead lead) throws SQLException
  {

  }

  @Override public void addMeetingListener(
      RemotePropertyChangeListener<Meeting> listener) throws RemoteException
  {
    meetingSupport.addPropertyChangeListener(listener);
  }

  @Override public void addTaskListener(
      RemotePropertyChangeListener<Task> listener) throws RemoteException
  {

  }

  @Override public void addLeadListener(
      RemotePropertyChangeListener<Lead> listener) throws RemoteException
  {

  }

  @Override public ArrayList<Meeting> getMeetings() throws SQLException
  {
    connection = SQLConnection.getInstance();
    return connection.getMeetings();
  }

  //Syncronization of Users




>>>>>>> Stashed changes
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
