package app.model;

import app.shared.Meeting;
import app.shared.Task;
import app.shared.User;
import app.shared.UserTableRow;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public interface Model
{
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String leadEmail,ArrayList<String> emails)
      throws SQLException, RemoteException;
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();

  void addTask(String title, String description, java.sql.Date date, String status, int business_id)
      throws SQLException, RemoteException;

  void editTask(Task newTask, Task oldTask) throws SQLException, RemoteException;
  void editMeeting(Meeting oldMeeting, Meeting newMeeting) throws SQLException, RemoteException;

  ArrayList<Task> getTasks();
  boolean checkUser();
  void meetingAddedFromServer()
      throws SQLException, RemoteException;

  void addPropertyChangeListener(PropertyChangeListener listener);
  void taskAddedFromServer() throws SQLException, RemoteException;
  void reloadUsers() throws SQLException, RemoteException;
  ArrayList<User> getUsers() throws SQLException, RemoteException;
  ArrayList<String> getAttendance(Meeting meeting)
      throws SQLException, RemoteException;
}
