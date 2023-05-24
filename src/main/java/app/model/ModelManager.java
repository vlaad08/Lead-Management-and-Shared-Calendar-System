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

  private User loggedInUser = new User("", "", "", "", "", true, "", 41412);

  private final PropertyChangeSupport support;



  public ModelManager(Communicator communicator)
  {
    this.communicator = communicator;

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
        if(expectedType.equalsIgnoreCase("meetings") || expectedType.equalsIgnoreCase("tasks"))
        {

          if(isManager())
          {

            return communicator.getList(expectedType);
          }
          else
          {

            return communicator.getListByUser(loggedInUser, expectedType);
          }
        }
        else
        {
          return communicator.getList(expectedType);
        }
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
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

  @Override public User getLoggedInUser()
  {
    return loggedInUser;
  }

  @Override public boolean isManager()
  {

    return loggedInUser.isManager();
  }

  @Override public void businessAddedFromServer()
  {
  }

  @Override public String getUserPassword(String oldEmail)
      throws SQLException, RemoteException
  {
    return communicator.getUserPassword(oldEmail);
  }

  @Override public void addObjectWithPassword(User user, String password)
      throws SQLException, RemoteException
  {
    communicator.addObjectWithPassword(user, password);
  }

  @Override public void editObjectWithPassword(Object oldObj, Object newObj, String password)
      throws SQLException, RemoteException
  {
    communicator.editObjectWithPassword(oldObj,newObj,password);
  }

  @Override public Object getObject(Object obj, String expectedType)
      throws SQLException, RemoteException
  {

    return communicator.getObject(obj, expectedType);
  }

  @Override public void meetingAddedFromServer()
  {
    support.firePropertyChange("reloadMeetings", false, true);

  }

  @Override public void leadAddedFromServer()
  {
    support.firePropertyChange("reloadLeads", false, true);
  }

  @Override public void userAddedFromServer()
  {
    support.firePropertyChange("reloadUser", false, true);
  }

  @Override public void taskAddedFromServer()
  {
    support.firePropertyChange("reloadTasks", false, true);
  }


}
