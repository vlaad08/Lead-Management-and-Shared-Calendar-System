package app.server;

import app.JDBC.SQLConnection;
import app.shared.*;

import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;

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



  @Override public void addListener(
      RemotePropertyChangeListener<String> listener) throws RemoteException
  {
    support.addPropertyChangeListener(listener);
  }

  private boolean checkIfAddressExists(Address address)
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


  @Override public int getBusinessId(Business business)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.getBusinessID(business);
  }



  @Override public boolean logIn(String email, String password)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.logIn(email, password);
  }

  @Override public User getUserByEmail(String email)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    return connection.getUserByEmail(email);
  }

  @Override public void addObject(Object obj)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();


    if(obj instanceof User)
    {
      connection.createUser((User) obj);
      support.firePropertyChange("reloadUser", null, "");
    }
    if(obj instanceof Lead)
    {
      connection.createLead((Lead) obj);
      support.firePropertyChange("reloadLead",null,"");
    }
    if(obj instanceof Address && !checkIfAddressExists((Address) obj))
    {
      connection.createAddress((Address) obj);
      support.firePropertyChange("reloadAddress", null, "");
    }
    if(obj instanceof Business)
    {
      connection.createBusiness((Business) obj);
      support.firePropertyChange("reloadBusiness",null,"");
    }
  }

  @Override public void addObject(Object obj, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    if(obj instanceof Meeting)
    {
      connection.createMeeting((Meeting) obj);
      for(String e : emails)
      {
        connection.setAttendance(e, (Meeting) obj);
      }
      support.firePropertyChange("reloadMeeting", null, "");
    }
    if(obj instanceof Task)
    {
      connection.createTask((Task) obj);
      for(String e : emails)
      {
        connection.assignTask((Task) obj, e);
      }
      support.firePropertyChange("reloadTask", null, "");
    }
  }

  @Override public ArrayList<Object> getList(String expectedType)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    if(expectedType.equalsIgnoreCase("Meetings"))
    {
      return connection.getMeetings();
    }
    if(expectedType.equalsIgnoreCase("Tasks"))
    {
      return connection.getTasks();
    }
    if(expectedType.equalsIgnoreCase("Users"))
    {
      return connection.getUsers();
    }
    if(expectedType.equalsIgnoreCase("Leads"))
    {
      return connection.getLeads();
    }
    if(expectedType.equalsIgnoreCase("Businesses"))
    {
      return connection.getBusinesses();
    }
    return new ArrayList<>();
  }


  @Override public ArrayList<String> getListOfEmployees(Object obj)
      throws SQLException, RemoteException
  {
    if(obj instanceof Meeting)
    {
      return connection.getAttendance((Meeting) obj);
    }
    if(obj instanceof Task)
    {
      return connection.getAssignedUsers((Task) obj);
    }
    return new ArrayList<>();
  }

  @Override public ArrayList<Object> getListOfAvailableEmployees(Date date, Time startTime,
      Time endTime) throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    ArrayList<Object> meetings = connection.getMeetings();
    ArrayList<Object> users = connection.getUsers();



    if(meetings != null)
    {
      Map<Meeting, ArrayList<String>> meetingMap = new HashMap<>();

      for(Object meeting : meetings)
      {
        meetingMap.put((Meeting) meeting, connection.getAttendance((Meeting) meeting));
      }

      for(Meeting meeting : meetingMap.keySet())
      {
        ArrayList<String> userEmails = meetingMap.get(meeting);


        for(String email : userEmails)
        {
          User user = connection.getUserByEmail(email);


          if(meeting.getDate().equals(date) &&
              (
                  (meeting.getStartTime().before(startTime) && meeting.getEndTime().before(endTime) && meeting.getEndTime().after(startTime))
                      || (meeting.getStartTime().after(startTime) && meeting.getEndTime().before(endTime))
                      || (meeting.getStartTime().before(startTime) && meeting.getEndTime().after(endTime))
                      || (meeting.getStartTime().after(startTime) && meeting.getEndTime().after(endTime) && meeting.getStartTime().before(endTime))
                      || (meeting.getStartTime().equals(startTime) && meeting.getEndTime().equals(endTime))
              )
          )
          {
            users.remove(user);
          }
        }
      }
    }
    return users;
  }

  @Override public void removeObject(Object obj)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    if(obj instanceof Meeting)
    {
      connection.removeAttendance((Meeting) obj);
      connection.removeMeeting((Meeting) obj);
      support.firePropertyChange("reloadMeeting", null, "");
    }
    if(obj instanceof Task)
    {
      connection.removeAssignedUsers((Task) obj);
      connection.removeTask((Task) obj);
      support.firePropertyChange("reloadTask", null, "");
    }
    if(obj instanceof Lead)
    {
      ArrayList<Meeting> meetings = connection.getMeetingsByLead((Lead) obj);

      for(Meeting meeting : meetings)
      {
        connection.removeAttendance(meeting);
        connection.removeMeeting(meeting);
      }

      connection.removeLead((Lead) obj);
      support.firePropertyChange("reloadLead", null, "");
      support.firePropertyChange("reloadMeeting", null, "");
    }
  }

  @Override public ArrayList<Object> getListByUser(User user,
      String expectedType) throws SQLException, RemoteException
  {
    if(expectedType.equalsIgnoreCase("meeting"))
    {
      connection.getMeetingsByUser(user);
    }
    if(expectedType.equalsIgnoreCase("tasks"))
    {
      connection.getTasksByUser(user);
    }
    return new ArrayList<>();
  }

  @Override public void editObject(Object oldObj, Object newObj)
      throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();
    if(oldObj instanceof Lead && newObj instanceof Lead)
    {
      ArrayList<Meeting> meetings = connection.getMeetingsByLead((Lead) oldObj);
      Map<Meeting,ArrayList<String>> meetingMap = new HashMap<>();
      for(Meeting meeting : meetings)
      {
        ArrayList<String> emails = getListOfEmployees(meeting);

        connection.removeAttendance(meeting);
        connection.removeMeeting(meeting);

        meeting.setLeadEmail(((Lead) newObj).getEmail());

        meetingMap.put(meeting, emails);
      }

      connection.editLead((Lead) oldObj, (Lead) newObj);

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

  @Override public void editObjectWithList(Object oldObj, Object newObj,
      ArrayList<String> emails) throws SQLException, RemoteException
  {
    connection = SQLConnection.getInstance();

    if(oldObj instanceof Task && newObj instanceof Task)
    {
      connection.removeAssignedUsers((Task) oldObj);
      connection.editTask((Task) newObj, (Task) oldObj);
      for(String e : emails)
      {
        connection.assignTask((Task) newObj, e);
      }
      support.firePropertyChange("reloadTask", null, "");
    }
    if(oldObj instanceof Meeting && newObj instanceof Meeting)
    {
      connection.removeAttendance((Meeting) oldObj);
      connection.editMeeting((Meeting) oldObj, (Meeting) newObj);
      for(String e : emails)
      {
        connection.setAttendance(e, (Meeting) newObj);
      }
      support.firePropertyChange("reloadMeeting", null, "");
    }
  }

}
