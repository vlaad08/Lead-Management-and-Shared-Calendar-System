package app.viewmodel;

import app.model.ModelMeetingAndTask;
import app.shared.Lead;
import app.shared.Meeting;
import app.shared.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
  private final ModelMeetingAndTask model;

  private final PropertyChangeSupport support;
  private final ObjectProperty<ObservableList<Meeting>> meetings;
  private SimpleStringProperty name;

  public MeetingViewModel(ModelMeetingAndTask model){
    this.model = model;
    model.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
    meetings = new SimpleObjectProperty<>();

    name = new SimpleStringProperty(model.getLoggedInUser().getFirstName());
    meetings.set(FXCollections.observableArrayList(getMeetings()));

  }

  public void bindUserName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public boolean isManager()
  {
    return model.isManager();
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
    Meeting meeting =new Meeting(title,description,date,startTime,endTime, leadEmail);

    model.addObject(meeting, emails);
  }

  public void editMeeting(Meeting oldMeeting, Meeting newMeeting, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    model.editObjectWithList(oldMeeting,newMeeting,emails);
  }

  public void removeMeeting(Meeting meeting)
      throws SQLException, RemoteException
  {
    model.removeObject(meeting);
  }

  public ArrayList<Meeting> getMeetings()
  {
    ArrayList<Meeting> meetings1 = new ArrayList<>();
    ArrayList<Object> objects = model.getList("meetings");


    for(Object obj : objects)
    {
      if(obj instanceof Meeting)
      {
        meetings1.add((Meeting) obj);
      }
    }


    return meetings1;
  }


  public ArrayList<Lead> getLeads()
  {
    ArrayList<Lead> leads = new ArrayList<>();
    ArrayList<Object> objects = model.getList("leads");


    for(Object obj : objects)
    {
      if(obj instanceof Lead)
      {
        leads.add((Lead) obj);
      }
    }
    return leads;
  }

  public ArrayList<User> getUsers()
  {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Object> objects = model.getList("users");


    for(Object obj : objects)
    {
      if(obj instanceof User)
      {
        users.add((User) obj);
      }
    }
    return users;

  }

  public ArrayList<User> getAvailableUsers(Date date, Time startTime, Time endTime)
      throws SQLException, RemoteException
  {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Object> objects = model.getList(date,startTime,endTime);


    for(Object obj : objects)
    {
      if(obj instanceof User)
      {
        users.add((User) obj);
      }
    }
    return users;
  }

  public ArrayList<String> getAttendance(Meeting meeting)
  {
    return model.getList(meeting);
  }



  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings"))
    {
      ArrayList<Meeting> list = getMeetings();



      ObservableList<Meeting> observableList= FXCollections.observableList(list);
      meetings.set(observableList);
      support.firePropertyChange("reloadMeetings", false, true);
    }

    if(evt.getPropertyName().equals("reloadLoggedInUser"))
    {
      name = new SimpleStringProperty(model.getLoggedInUser().getFirstName());
    }
  }
}
