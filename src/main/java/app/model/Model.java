package app.model;

import app.shared.*;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public interface Model extends ModelMeetingAndTask, ModelCalendar, ModelUser, ModelLead, ModelLogin, ModelListener
{



  void addObject(Object obj) throws SQLException, RemoteException;

  void addObject(Object obj, ArrayList<String> emails)
      throws SQLException, RemoteException;

  ArrayList<Object> getList(String expectedType);

  ArrayList<String> getList(Object obj);
  ArrayList<Object> getList(Date date, Time startTime, Time endTime)
      throws SQLException, RemoteException;

  void removeObject(Object obj) throws SQLException, RemoteException;

  void editObject(Object oldObj, Object newObj) throws SQLException, RemoteException;

  void editObjectWithList(Object oldObj, Object newObj, ArrayList<String> emails) throws
      SQLException, RemoteException;


//-----------




  void addPropertyChangeListener(PropertyChangeListener listener);
  boolean isManager();

  void meetingAddedFromServer()
      throws SQLException, RemoteException;

  void taskAddedFromServer() throws SQLException, RemoteException;

  void leadAddedFromServer() throws SQLException, RemoteException;

  void userAddedFromServer() throws SQLException, RemoteException;
  boolean logIn(String email, String password)
      throws SQLException, RemoteException;
  int getBusinessId(Business business) throws SQLException, RemoteException;
  User getLoggedInUser();


  String getUserPassword(String oldEmail) throws SQLException, RemoteException;
  void addObjectWithPassword(User user, String password) throws SQLException, RemoteException;
  void editObjectWithPassword(Object oldObj, Object newObj, String password)
      throws SQLException, RemoteException;
  Object getObject(Object obj, String expectedType) throws SQLException, RemoteException;
}
