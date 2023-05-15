package app.model;

import app.shared.Meeting;
import app.shared.Task;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public interface Model
{
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email)
      throws SQLException, RemoteException;
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();
  void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description, ArrayList<User> employees);

  void addTask(String title, String description, java.sql.Date date, String status, int business_id)
      throws SQLException, RemoteException;

  void editTask(Task newTask, Task oldTask) throws SQLException, RemoteException;

  ArrayList<Task> getTasks();
  boolean checkUser();
  void setUser();
  void meetingAddedFromServer()
      throws SQLException, RemoteException;

  void addPropertyChangeListener(PropertyChangeListener listener);
  void taskAddedFromServer() throws SQLException, RemoteException;
}
