package app.model;

import app.shared.User;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public interface ModelMeetingAndTask
{
  void addObject(Object obj, ArrayList<String> emails)
      throws SQLException, RemoteException;
  void removeObject(Object obj) throws SQLException, RemoteException;
  void editObjectWithList(Object oldObj, Object newObj, ArrayList<String> emails) throws
      SQLException, RemoteException;
  ArrayList<Object> getList(String expectedType);
  ArrayList<String> getList(Object obj);
  ArrayList<Object> getList(Date date, Time startTime, Time endTime)
      throws SQLException, RemoteException;
  void addPropertyChangeListener(PropertyChangeListener listener);
  User getLoggedInUser();
  boolean isManager();
}
