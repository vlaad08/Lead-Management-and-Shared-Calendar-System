package app.model;

import app.shared.Meeting;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public interface Model
{
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email);
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();
<<<<<<< Updated upstream
  void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description, ArrayList<User> employees);
=======
  void editMeeting(Date oldStartDate, Date oldEndDate, Date startDate, Date endDate, String description, ArrayList<User> employees);
  boolean checkUser();
  void setUser();

  void meetingAddedFromServer(Meeting meeting)
      throws SQLException, RemoteException;

  void addPropertyChangeListener(PropertyChangeListener listener);

  void reloadMeetings() throws SQLException, RemoteException;
>>>>>>> Stashed changes
}
