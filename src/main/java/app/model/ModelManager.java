package app.model;

import app.JDBC.SQLConnection;
import app.shared.Communicator;
import app.shared.Meeting;
import app.shared.Task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelManager implements Model
{
  private Communicator communicator;

  private User user;

  private ArrayList<Meeting> meetings;
  private ArrayList<Task> tasks;

  private final PropertyChangeSupport support;

  public ModelManager(Communicator communicator)
      throws SQLException, RemoteException
  {
    this.communicator = communicator;

    meetings = communicator.getMeetings();
    tasks = communicator.getTasks();


    support = new PropertyChangeSupport(this);

    user=new User("employee-1", "password", "wasdwasd@1234.com","Craig","Larhman", false);
  }

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email)
      throws SQLException, RemoteException
  {
    communicator.createMeeting(new Meeting(title, description, date, startTime, endTime, email));
  }

  public void setUser()
  {
    user.setManager(true);
  }

  @Override public void meetingAddedFromServer()
      throws SQLException, RemoteException
  {
    reloadLists();
    support.firePropertyChange("reloadMeetings", false, true);
  }



  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void taskAddedFromServer()
      throws SQLException, RemoteException
  {
    reloadLists();
    support.firePropertyChange("reloadTasks", false, true);
  }

  @Override public void removeMeeting(Meeting meeting)
  {

  }

  @Override public ArrayList<Meeting> getMeetings() {
    try{
      reloadLists();
      return meetings;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override public void editMeeting(Meeting oldMeeting, Meeting newMeeting)
  {

  }

  @Override public void addTask(String title, String description,
      java.sql.Date date, String status, int business_id)
      throws SQLException, RemoteException
  {
    communicator.createTask(new Task(title, description, date, status, business_id));
  }

  @Override public ArrayList<Task> getTasks()
  {
    try{
      reloadLists();
      return tasks;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override public boolean checkUser()
  {
    return false;
  }

  private void reloadLists() throws SQLException, RemoteException
  {
    meetings = communicator.getMeetings();
    tasks = communicator.getTasks();
  }
}
