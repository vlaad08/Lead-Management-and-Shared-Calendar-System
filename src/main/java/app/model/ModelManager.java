package app.model;

import app.shared.*;
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


  private ArrayList<Object> meetings;
  private ArrayList<Object> tasks;
  private ArrayList<Object> users;
  private ArrayList<Object> businesses;
  private ArrayList<Object> leads;

  private User loggedInUser = new User("", "", "", "", "", true, "", 41412, "", "");

  private final PropertyChangeSupport support;

  public ModelManager(Communicator communicator)
  {
    this.communicator = communicator;

    meetings = getList("meeting");
    tasks = getList("tasks");
    users = getList("users");
    leads = getList("leads");
    businesses = getList("businesses");


    support = new PropertyChangeSupport(this);

  }

  @Override public void addObject(Object obj)
      throws SQLException, RemoteException
  {
    communicator.addObject(obj);
  }

  @Override public void addObject(Object obj, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    communicator.addObject(obj, emails);
  }


  @Override public ArrayList<Object> getList(String expectedType)
  {
      try
      {
        return  communicator.getList(expectedType);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException();
      }

  }

  @Override public ArrayList<String> getList(Object obj)
  {
    try
    {
      return communicator.getListOfEmployees(obj);
    }
    catch (SQLException | RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  @Override public ArrayList<Object> getList(Date date, Time startTime,
      Time endTime) throws SQLException, RemoteException
  {
    return communicator.getListOfAvailableEmployees(date, startTime, endTime);
  }

  @Override public void removeObject(Object obj)
    throws SQLException, RemoteException
  {
    communicator.removeObject(obj);
  }

  @Override public void editObject(Object oldObj, Object newObj)
      throws SQLException, RemoteException
  {
    communicator.editObject(oldObj,newObj);
  }

  @Override public void editObjectWithList(Object oldObj, Object newObj,
      ArrayList<String> emails) throws SQLException, RemoteException
  {
    communicator.editObjectWithList(oldObj,newObj,emails);
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }


  @Override public int getBusinessId(Business business)
      throws SQLException, RemoteException
  {
    return communicator.getBusinessId(business);
  }



  @Override public boolean logIn(String email, String password)
      throws SQLException, RemoteException
  {
    if(communicator.logIn(email, password))
    {
      loggedInUser = communicator.getUserByEmail(email);
      support.firePropertyChange("reloadMeetings", false, true);
      support.firePropertyChange("reloadTasks", false, true);
      support.firePropertyChange("reloadLoggedInUser", false, true);
      return true;
    }
    return false;
  }

  @Override public String getLoggedInUserName()
  {
    return loggedInUser.getFirstName();
  }


  @Override public boolean isManager()
  {

    return loggedInUser.isManager();
  }

  @Override public void businessAddedFromServer()
  {
   businesses = getList("businesses");
  }

  @Override public void meetingAddedFromServer()
  {
    meetings = getList("meetings");
    support.firePropertyChange("reloadMeetings", false, true);
  }

  @Override public void leadAddedFromServer()
  {
    leads = getList("leads");
    support.firePropertyChange("reloadLeads", false, true);
  }

  @Override public void userAddedFromServer()
  {
    users = getList("users");
    support.firePropertyChange("reloadUsers", false, true);
  }

  @Override public void taskAddedFromServer()
  {
    tasks = getList("tasks");
    support.firePropertyChange("reloadTasks", false, true);
  }


}
