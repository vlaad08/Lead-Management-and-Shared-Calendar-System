package app.model;

import app.JDBC.SQLConnection;
import app.shared.Communicator;
import app.shared.Meeting;
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

  private PropertyChangeSupport support;

  public ModelManager(Communicator communicator)
      throws SQLException, RemoteException
  {
    this.communicator = communicator;
    meetings = communicator.getMeetings();
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
    reloadMeetings();
    support.firePropertyChange("reloadMeetings", false, true);
  }



  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void reloadMeetings() throws SQLException, RemoteException
  {
    meetings = communicator.getMeetings();
  }

  @Override public void removeMeeting(Meeting meeting)
  {

  }

  @Override public ArrayList<Meeting> getMeetings() {
    try{
      reloadMeetings();
      return meetings;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override public void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description,
      ArrayList<User> employees)
  {

  }

  @Override public boolean checkUser()
  {
    return false;
  }
}
