package app.model;

import app.shared.Meeting;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface Model
{
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email);
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();
  void editMeeting(Date oldStartDate, Date oldEndDate, Date startDate, Date endDate, String description, ArrayList<User> employees);
  boolean checkUser();
  void setUser();
  PropertyChangeSupport getSupport();
  void addPropertyChangeListener(PropertyChangeListener listener);
}
