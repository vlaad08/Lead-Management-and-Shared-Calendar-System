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





  boolean isManager();

  void meetingAddedFromServer()
      throws SQLException, RemoteException;

  void addPropertyChangeListener(PropertyChangeListener listener);
  void taskAddedFromServer() throws SQLException, RemoteException;




  int getBusinessId(Business business) throws SQLException, RemoteException;
  void leadAddedFromServer() throws SQLException, RemoteException;

  void userAddedFromServer() throws SQLException, RemoteException;
  boolean logIn(String email, String password)
      throws SQLException, RemoteException;

  String getLoggedInUserName();
  void businessAddedFromServer();

}
