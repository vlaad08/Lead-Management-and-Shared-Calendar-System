package app.shared;

import dk.via.remote.observer.RemotePropertyChangeListener;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public interface Communicator extends Remote
{
  void createMeeting(Meeting meeting) throws SQLException, RemoteException, NullPointerException;
  void createTask(Task task) throws SQLException, RemoteException;
  void createLead(Lead lead) throws SQLException, RemoteException;
  void removeMeeting(Meeting meeting) throws SQLException, RemoteException;
  void removeTask(Task task) throws SQLException, RemoteException;
  void removeLead(Lead lead) throws SQLException, RemoteException;
  void editTask(Task newTask, Task oldTask) throws SQLException, RemoteException;
  void editMeeting(Meeting oldMeeting, Meeting newMeeting) throws SQLException, RemoteException;
  void addListener(RemotePropertyChangeListener<String> listener) throws
      RemoteException;
  ArrayList<Meeting> getMeetings() throws RemoteException, SQLException;

  ArrayList<Task> getTasks() throws RemoteException, SQLException;
  ArrayList<User> getUsers() throws RemoteException, SQLException;

  ArrayList<User> getAvailableUsers(Date date, Time startTime, Time endTime) throws RemoteException, SQLException;
  void attendsMeeting(String email, Meeting meeting) throws SQLException, RemoteException;

  ArrayList<String> getAttendance(Meeting meeting) throws SQLException, RemoteException;

  ArrayList<Lead> getLeads() throws SQLException, RemoteException;
  void removeAttendance(Meeting oldMeeting) throws SQLException, RemoteException;

  ArrayList<Business> getBusinesses() throws SQLException, RemoteException;
  void assignTask(String email, Task task) throws SQLException, RemoteException;
  ArrayList<String> getAssignedUsers(Task task) throws SQLException, RemoteException;
  void removeAssignedUsers(Task task) throws SQLException, RemoteException;
  void createAddress(Address address)
      throws SQLException, RemoteException;
  boolean checkIfAddressExists(Address address) throws SQLException, RemoteException;
  void createBusiness(Business business) throws SQLException, RemoteException;
  int getBusinessId(Business business) throws SQLException, RemoteException;
  void editLead(Lead oldLead, Lead newLead) throws SQLException,
      RemoteException;
  void addUser(User user) throws SQLException;
}
