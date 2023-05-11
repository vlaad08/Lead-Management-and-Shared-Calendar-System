package app.model;

import app.JDBC.SQLConnection;
import app.shared.Communicator;
import app.shared.Meeting;

<<<<<<< Updated upstream
=======
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
>>>>>>> Stashed changes
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelManager implements Model
{
<<<<<<< Updated upstream
  private ClientListener clientListener;
  public ModelManager(ClientListener clientListener){
    this.clientListener = clientListener;
  }

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email) {
   try{
     clientListener.addMeeting(new Meeting(title, description, date, startTime, endTime, email));
   }catch (Exception e){
     e.printStackTrace();
   }
=======
  private User user;
  private Communicator communicator;

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

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email) {

      try{
        communicator.createMeeting(new Meeting(title, description, date, startTime, endTime, email));
      }catch (Exception e){
        e.printStackTrace();
      }


  }

  public void setUser()
  {
    user.setManager(true);
>>>>>>> Stashed changes
  }

  @Override public void meetingAddedFromServer(Meeting meeting)
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
}
