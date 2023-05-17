package app.server;

import app.JDBC.SQLConnection;
import app.shared.*;

import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerImplementation implements Communicator
{
  private final RemotePropertyChangeSupport<String> support;

  private SQLConnection connection;



  public ServerImplementation()
  {
    support = new RemotePropertyChangeSupport<>();
  }


  @Override public void createMeeting(Meeting meeting) throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.createMeeting(meeting);
    support.firePropertyChange("reloadMeeting", null, "");
  }

  @Override public void createTask(Task task)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.createTask(task);
    support.firePropertyChange("reloadTask", null, "");
  }

  @Override public void createLead(Lead lead)
      throws SQLException, RemoteException
  {

  }

  @Override public void removeMeeting(Meeting meeting)
      throws SQLException, RemoteException
  {

  }

  @Override public void removeTask(Task task)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    removeAssignedUsers(task);
    connection.removeTask(task);
    support.firePropertyChange("reloadTask", null, "");
  }

  @Override public void removeLead(Lead lead) throws SQLException
  {

  }

  @Override public void editTask(Task newTask, Task oldTask) throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.editTask(newTask, oldTask);
    support.firePropertyChange("reloadTask", null, "");
  }
  @Override public void editMeeting(Meeting oldMeeting, Meeting newMeeting) throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.editMeeting(oldMeeting,newMeeting);
    support.firePropertyChange("reloadMeeting",null,"");
  }

  @Override public void addListener(
      RemotePropertyChangeListener<String> listener) throws RemoteException
  {
    support.addPropertyChangeListener(listener);
  }



  @Override public ArrayList<Meeting> getMeetings() throws SQLException
  {
    connection = SQLConnection.getInstance();
    return connection.getMeetings();
  }

  @Override public ArrayList<Task> getTasks()
      throws RemoteException, SQLException
  {
    connection = SQLConnection.getInstance();
    return connection.getTasks();
  }

  @Override public ArrayList<User> getUsers()
      throws RemoteException, SQLException
  {
    connection = SQLConnection.getInstance();
    return connection.getUsers();
  }

  @Override public void attendsMeeting(String email, Meeting meeting) throws SQLException
  {
    connection = SQLConnection.getInstance();
    connection.setAttendance(email,meeting);
  }

  @Override public ArrayList<String> getAttendance(Meeting meeting)
      throws SQLException
  {
    connection = SQLConnection.getInstance();
    return connection.getAttendance(meeting);
  }


  @Override public ArrayList<Lead> getLeads()
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.getLeads();
  }

  @Override public void removeAttendance(Meeting oldMeeting)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.removeAttendance(oldMeeting);
  }

  @Override public ArrayList<Business> getBusinesses()
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.getBusinesses();
  }

  @Override public void assignTask(String email, Task task)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.assignTask(task, email);
  }

  @Override public ArrayList<String> getAssignedUsers(Task task)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.getAssignedUsers(task);
  }

  @Override public void removeAssignedUsers(Task task)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.removeAssignedUsers(task);
  }

  @Override public void addLead(Lead lead) throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.createLead(lead);
    support.firePropertyChange("reloadLeads",null,"");
  }

  @Override public void createAddress(Address address) throws SQLException
  {
    connection = SQLConnection.getInstance();
    connection.createAddress(address);
  }

  @Override public boolean checkIfAddressExists(Address address)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    Address a = connection.getAddress(address);

    return a.equals(address);
  }

  @Override public void createBusiness(Business business)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.createBusiness(business);
    support.firePropertyChange("reloadBusiness",null,"");
  }

  @Override public int getBusinessId(Business business)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.getBusinessID(business);
  }

  //Syncronization of Users


  /*
  @Override public void addTask(Task task) throws RemoteException
  {
    taskList.add(task);
    supportTask.firePropertyChange("task",null,task);
  }

  @Override public void manageTask(Task deletedTask, Task createdTask) throws RemoteException
  {
    taskList.remove(deletedTask);
    taskList.add(createdTask);
    supportTask.firePropertyChange("task",null,createdTask);
  }

  @Override public void removeTask(Task task) throws RemoteException
  {
    taskList.remove(task);
    supportTask.firePropertyChange("task",null,task);
  }

  @Override public ArrayList<Task> getTasks() throws RemoteException
  {
    return taskList;
  }

  @Override public void addLead(Lead lead) throws RemoteException
  {
    leadList.add(lead);
    supportLead.firePropertyChange("lead",null,lead);
  }

  @Override public void manageLead(Lead deletedLead, Lead createdLead) throws RemoteException
  {
    leadList.remove(deletedLead);
    leadList.add(createdLead);
    supportLead.firePropertyChange("lead",null,createdLead);
  }

  @Override public void removeLead(Lead lead) throws RemoteException
  {
    leadList.remove(lead);
    supportLead.firePropertyChange("lead",null,lead);
  }

  @Override public ArrayList<Lead> getLeads() throws RemoteException
  {
    return leadList;
  }

   */
}
