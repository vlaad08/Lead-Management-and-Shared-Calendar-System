package app.model;

import app.shared.Communicator;
import app.shared.Meeting;
import app.shared.Task;
import app.shared.User;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class ModelManager implements Model
{
  private Communicator communicator;

  private User user;

  private ArrayList<Meeting> meetings;
  private ArrayList<Task> tasks;
  private ArrayList<User> users;

  private final PropertyChangeSupport support;

  public ModelManager(Communicator communicator)
      throws SQLException, RemoteException
  {
    this.communicator = communicator;

    meetings = communicator.getMeetings();
    tasks = communicator.getTasks();
    users = communicator.getUsers();


    support = new PropertyChangeSupport(this);

    user  =new User("Craig", "", "Larhman", "vaoiodiw@gmail.com", "40182241", true, "Stefan Cel Mare", 8700);
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
    meetings = communicator.getMeetings();
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
    tasks = communicator.getTasks();
    support.firePropertyChange("reloadTasks", false, true);
  }

  @Override public void reloadUsers() throws SQLException, RemoteException
  {
    users = communicator.getUsers();
    support.firePropertyChange("reloadUsers", false, true);
  }

  @Override public ArrayList<User> getUsers()
      throws SQLException, RemoteException
  {
    reloadUsers();
    return users;
  }

  @Override public void removeMeeting(Meeting meeting)
  {

  }

  @Override public ArrayList<Meeting> getMeetings() {
    try{
      meetings = communicator.getMeetings();
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

  @Override public void editTask(Task newTask, Task oldTask)
      throws SQLException, RemoteException
  {
    communicator.editTask(newTask, oldTask);
  }

  @Override public ArrayList<Task> getTasks()
  {
    try{
      tasks = communicator.getTasks();
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


}
