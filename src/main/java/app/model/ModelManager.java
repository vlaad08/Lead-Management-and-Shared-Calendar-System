package app.model;

import app.shared.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class ModelManager implements Model
{
  private final Communicator communicator;


  private ArrayList<Meeting> meetings;
  private ArrayList<Task> tasks;
  private ArrayList<User> users;
  private ArrayList<Business> businesses;
  private ArrayList<Lead> leads;

  private final PropertyChangeSupport support;

  public ModelManager(Communicator communicator)
      throws SQLException, RemoteException
  {
    this.communicator = communicator;

    meetings = communicator.getMeetings();
    tasks = communicator.getTasks();
    users = communicator.getUsers();
    leads = communicator.getLeads();
    businesses = communicator.getBusinesses();


    support = new PropertyChangeSupport(this);


  }

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String leadEmail,ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    Meeting meeting = new Meeting(title, description, date, startTime, endTime, leadEmail);


    communicator.createMeeting(meeting);
    for(String email: emails)
    {
      communicator.attendsMeeting(email, meeting);
    }

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


  @Override public ArrayList<User> getUsers()
  {
    try{
      users = communicator.getUsers();
      return users;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override public ArrayList<String> getAttendance(Meeting meeting)
      throws SQLException, RemoteException
  {
    return communicator.getAttendance(meeting);
  }

  @Override public ArrayList<Lead> getLeads()
  {
    try{
      leads = communicator.getLeads();
      return leads;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override public ArrayList<Business> getBusinesses()
      throws SQLException, RemoteException
  {
    businesses = communicator.getBusinesses();
    return businesses;
  }

  @Override public ArrayList<String> getAssignedUsers(Task task)
      throws SQLException, RemoteException
  {
    return communicator.getAssignedUsers(task);
  }

  @Override public void removeTask(Task tasks)
      throws SQLException, RemoteException
  {
    communicator.removeTask(tasks);
  }

  @Override public void createLead(Lead lead) throws SQLException, RemoteException
  {
    communicator.createLead(lead);
  }

  @Override public void createAddress(String street, String city,
      String country, String postalCode) throws SQLException, RemoteException
  {


    Address address = new Address(street, city, country,
        Integer.parseInt(postalCode));

      communicator.createAddress(address);
  }

  @Override public void createBusiness(String businessName, String street,
      String postalCode) throws SQLException, RemoteException
  {
    Business business = new Business(businessName, street, Integer.parseInt(postalCode));

    communicator.createBusiness(business);
  }

  @Override public int getBusinessId(Business business)
      throws SQLException, RemoteException
  {
    return communicator.getBusinessId(business);
  }

  @Override public void leadAddedFromServer()
      throws SQLException, RemoteException
  {
    leads = communicator.getLeads();
    support.firePropertyChange("reloadLeads", false, true);
  }

  @Override public void editLead(Lead oldLead, Lead newLead)
      throws SQLException, RemoteException
  {
    communicator.editLead(oldLead, newLead);
  }

  @Override public void removeLead(Lead lead)
      throws SQLException, RemoteException
  {
    communicator.removeLead(lead);
  }

  @Override public ArrayList<User> getAvailableUsers(Date date, Time startTime,
      Time endTime) throws SQLException, RemoteException
  {
    return communicator.getAvailableUsers(date, startTime, endTime);
  }

  @Override public void addUser(User user) throws SQLException, RemoteException
  {
    communicator.addUser(user);
  }

  @Override public void userAddedFromServer()
      throws SQLException, RemoteException
  {
    users = communicator.getUsers();
    support.firePropertyChange("reloadUser", false, true);
  }

  @Override public void removeMeeting(Meeting meeting)
      throws SQLException, RemoteException
  {
    communicator.removeMeeting(meeting);
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

  @Override public void editMeeting(Meeting oldMeeting, Meeting newMeeting, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    communicator.removeAttendance(oldMeeting);
    communicator.editMeeting(oldMeeting, newMeeting);
    for(String e : emails)
    {
      communicator.attendsMeeting(e, newMeeting);
    }
  }

  @Override public void addTask(String title, String description,
      java.sql.Date date, String status, int business_id, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    Task task = new Task(title, description, date, status, business_id);


    communicator.createTask(task);
    for(String e : emails)
    {
      communicator.assignTask(e, task);
    }
  }

  @Override public void editTask(Task newTask, Task oldTask, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    communicator.removeAssignedUsers(oldTask);
    communicator.editTask(newTask, oldTask);
    for(String e : emails)
    {
      communicator.assignTask(e, newTask);
    }
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
