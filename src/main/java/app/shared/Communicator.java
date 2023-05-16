package app.shared;

import dk.via.remote.observer.RemotePropertyChangeListener;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface Communicator extends Remote
{
  void createMeeting(Meeting meeting) throws SQLException, RemoteException;
  void createTask(Task task) throws SQLException, RemoteException;
  void createLead(Lead lead) throws SQLException, RemoteException;
  void removeMeeting(Meeting meeting) throws SQLException, RemoteException;
  void removeTask(Task task) throws SQLException, RemoteException;
  void removeLead(Lead lead) throws SQLException, RemoteException;
  void editTask(Task newTask, Task oldTask) throws SQLException, RemoteException;
  void addMeetingListener(RemotePropertyChangeListener<Meeting> listener) throws
      RemoteException;
  void addTaskListener(RemotePropertyChangeListener<Task> listener) throws RemoteException;
  void addLeadListener(RemotePropertyChangeListener<Lead> listener) throws RemoteException;
  void addUserListener(RemotePropertyChangeListener<User> listener) throws RemoteException;

  ArrayList<Meeting> getMeetings() throws RemoteException, SQLException;

  ArrayList<Task> getTasks() throws RemoteException, SQLException;
  ArrayList<User> getUsers() throws RemoteException, SQLException;
  void attendsMeeting(String email, Meeting meeting) throws SQLException, RemoteException;

  ArrayList<String> getAttendance(Meeting meeting) throws SQLException, RemoteException;
  void editMeeting(Meeting oldMeeting, Meeting newMeeting) throws SQLException, RemoteException;
}
