package app.viewmodel;

import app.model.Model;
import app.shared.Lead;
import app.shared.Meeting;
import app.shared.User;
import app.shared.UserTableRow;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;

public class MeetingViewModel implements PropertyChangeListener
{
  private final Model model;

  private final PropertyChangeSupport support;
  private final ObjectProperty<ObservableList<Meeting>> meetings;

  public MeetingViewModel(Model model){
    this.model = model;
    model.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
    meetings = new SimpleObjectProperty<>();
    meetings.set(FXCollections.observableArrayList(getMeetings()));
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  public void bindMeetings(ObjectProperty<ObservableList<Meeting>> property)
  {
    property.bindBidirectional(meetings);
  }

  public void addMeeting(String title,String description,Date date, Time startTime,Time endTime, String leadEmail,ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    model.addMeeting(title,description,date,startTime,endTime, leadEmail, emails);
  }

  public void editMeeting(Meeting oldMeeting, Meeting newMeeting, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    model.editMeeting(oldMeeting, newMeeting, emails);
  }

  public void removeMeeting(Meeting meeting)
      throws SQLException, RemoteException
  {
    model.removeMeeting(meeting);
  }

  public ArrayList<Meeting> getMeetings()
  {
    return model.getMeetings();
  }

  public ArrayList<Lead> getLeads() throws SQLException, RemoteException
  {
    return model.getLeads();
  }

  public ArrayList<User> getUsers() throws SQLException, RemoteException
  {return model.getUsers();}

  public ArrayList<User> getAvailableUsers(Date date, Time startTime, Time endTime)
      throws SQLException, RemoteException
  {
    return model.getAvailableUsers(date, startTime, endTime);
  }

  public ArrayList<String> getAttendance(Meeting meeting)
      throws SQLException, RemoteException
  {
    return model.getAttendance(meeting);
  }


  public boolean checkUser()
  {
    return model.checkUser();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings"))
    {
      ArrayList<Meeting> list = model.getMeetings();
      ObservableList<Meeting> observableList= FXCollections.observableList(list);
      meetings.set(observableList);
      support.firePropertyChange("reloadMeetings", false, true);
    }
  }
}
