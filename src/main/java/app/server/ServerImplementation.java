package app.server;

import app.JDBC.SQLConnection;
import app.shared.*;

import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;
import org.postgresql.util.OSUtil;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    connection = SQLConnection.getInstance();
    connection.createLead(lead);
    support.firePropertyChange("reloadLead",null,"");
  }

  @Override public void removeMeeting(Meeting meeting)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.removeAttendance(meeting);
    connection.removeMeeting(meeting);

    support.firePropertyChange("reloadMeeting", null, "");
  }

  @Override public void removeTask(Task task)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    connection.removeAssignedUsers(task);
    connection.removeTask(task);
    support.firePropertyChange("reloadTask", null, "");
  }

  @Override public void removeLead(Lead lead)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    ArrayList<Meeting> meetings = connection.getMeetingsByLead(lead);

    for(Meeting meeting : meetings)
    {
      connection.removeAttendance(meeting);
      connection.removeMeeting(meeting);
    }

    connection.removeLead(lead);
    support.firePropertyChange("reloadLead", null, "");
    support.firePropertyChange("reloadMeeting", null, "");
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

  @Override public ArrayList<User> getAvailableUsers(Date date, Time startTime, Time endTime)
      throws RemoteException, SQLException
  {
    connection = SQLConnection.getInstance();

    ArrayList<Meeting> meetings = connection.getMeetings();
    ArrayList<User> users = getUsers();
    System.out.println(users);


    if(meetings != null)
    {
      Map<Meeting, ArrayList<String>> meetingMap = new HashMap<>();

      for(Meeting meeting : meetings)
      {
        meetingMap.put(meeting, connection.getAttendance(meeting));
      }

      for(Meeting meeting : meetingMap.keySet())
      {
        ArrayList<String> userEmails = meetingMap.get(meeting);


        for(String email : userEmails)
        {
          User user = connection.getUserByEmail(email);
          System.out.println(user);

          if(meeting.getDate().equals(date))
          {
            if(meeting.getStartTime().before(startTime))
            {
              System.out.println(users);
              users.remove(user);
              System.out.println(users);
            }
          }
          else if(meeting.getDate().before(date))
          {
            System.out.println(users);
            users.remove(user);
            System.out.println(users);
          }
        }
      }
    }
    return users;
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


  @Override public void createAddress(Address address)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    if(checkIfAddressExists(address))
    {
      connection.createAddress(address);
    }
  }

  @Override public boolean checkIfAddressExists(Address address)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    Address a = connection.getAddress(address);


    if(a != null)
    {
      return a.equals(address);
    }
    return false;
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

  @Override public void editLead(Lead oldLead, Lead newLead)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    ArrayList<Meeting> meetings = connection.getMeetingsByLead(oldLead);
    Map<Meeting,ArrayList<String>> meetingMap = new HashMap<>();
    for(Meeting meeting : meetings)
    {
      ArrayList<String> emails = getAttendance(meeting);

      connection.removeAttendance(meeting);
      connection.removeMeeting(meeting);

      meeting.setLeadEmail(newLead.getEmail());

      meetingMap.put(meeting, emails);
    }

    connection.editLead(oldLead, newLead);

    for (Meeting meeting : meetingMap.keySet())
    {
      connection.createMeeting(meeting);
      ArrayList<String> emails = meetingMap.get(meeting);
      for(String e : emails)
      {
        connection.setAttendance(e, meeting);
      }
    }

    support.firePropertyChange("reloadLead", null, "");
    support.firePropertyChange("reloadMeeting", null, "");
  }

}
