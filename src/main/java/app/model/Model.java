package app.model;

import app.shared.*;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public interface Model
{
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String leadEmail,ArrayList<String> emails)
      throws SQLException, RemoteException;
  void removeMeeting(Meeting meeting) throws SQLException, RemoteException;
  ArrayList<Meeting> getMeetings();

  void addTask(String title, String description, java.sql.Date date, String status, int business_id, ArrayList<String> emails)
      throws SQLException, RemoteException;

  void editTask(Task newTask, Task oldTask, ArrayList<String> emails) throws SQLException, RemoteException;
  void editMeeting(Meeting oldMeeting, Meeting newMeeting, ArrayList<String> emails) throws SQLException, RemoteException;

  ArrayList<Task> getTasks();
  boolean checkUser();
  void meetingAddedFromServer()
      throws SQLException, RemoteException;

  void addPropertyChangeListener(PropertyChangeListener listener);
  void taskAddedFromServer() throws SQLException, RemoteException;
  ArrayList<User> getUsers();
  ArrayList<String> getAttendance(Meeting meeting)
      throws SQLException, RemoteException;
  ArrayList<String> getAvailableUserForMeeting(Date date,Time startTime, Time endTime) throws SQLException, RemoteException;

  ArrayList<Lead> getLeads() ;

  ArrayList<Business> getBusinesses() throws SQLException, RemoteException;

  ArrayList<String> getAssignedUsers(Task task) throws SQLException, RemoteException;
  void removeTask(Task tasks) throws SQLException, RemoteException;
  void createLead(Lead lead) throws SQLException, RemoteException;
  void createAddress(String street, String city, String country, String postalCode)
      throws SQLException, RemoteException;
  void createBusiness(String businessName, String street, String postalCode) throws SQLException, RemoteException;
  int getBusinessId(Business business) throws SQLException, RemoteException;
  void leadAddedFromServer() throws SQLException, RemoteException;
  void editLead(Lead oldLead, Lead newLead) throws SQLException,
      RemoteException;
  void removeLead(Lead lead) throws SQLException, RemoteException;
  ArrayList<User> getAvailableUsers(Date date, Time startTime, Time endTime)
      throws SQLException, RemoteException;
  void addUser(User user) throws SQLException, RemoteException;
  void userAddedFromServer() throws SQLException, RemoteException;
}
