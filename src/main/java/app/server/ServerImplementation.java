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
  private int readers;
  private int writers;
  private int writersWaiting;

  public ServerImplementation(){
    meetingList = new ArrayList<>();
    support = new RemotePropertyChangeSupport<>();

    this.readers = 0;
    this.writers = 0;
    this.writersWaiting = 0;

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
      SQLConnection sqlConnection = requestWrite();
      sqlConnection.createMeeting(meeting.title(), meeting.description()
          ,meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
      support.firePropertyChange("meeting",null,meeting.toString());
    }catch (SQLException e){
      e.printStackTrace();
    }finally {
      releaseWrite();
    }
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
  public ArrayList<Meeting> getMeetings() throws RemoteException {
    try{
      SQLConnection sqlConnection = requestRead();
      return sqlConnection.getMeetings();
    }catch (SQLException e){
      e.printStackTrace();
    }finally {
      releaseRead();
    }
    return null;
  }

  @Override
  public void addTask(Task task)
  {
    try{
      SQLConnection sqlConnection = requestWrite();
      sqlConnection.addTask(task);
    }catch (SQLException e){
      e.printStackTrace();
    }finally {
      releaseWrite();
    }
  }

  @Override
  public ArrayList<Task> getTasks() throws RemoteException
  {
    return null;
  }

  //Syncronization of Users
  private synchronized SQLConnection requestRead(){
    while(writers > 0 || writersWaiting > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    readers++;
    return connection;
  }

  private synchronized void releaseRead(){
    readers--;
    if (readers == 0) {
      notifyAll();
    }
  }

  private synchronized SQLConnection requestWrite() {
    writersWaiting++;
    while(writers > 0 || readers > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    writers++;
    writersWaiting--;
    return connection;
  }

  private synchronized void releaseWrite() {
    writers--;
    if (writers == 0) {
      notifyAll();
    }
  }

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
