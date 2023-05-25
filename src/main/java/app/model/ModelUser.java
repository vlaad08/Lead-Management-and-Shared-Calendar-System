package app.model;

import app.shared.User;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ModelUser
{
  void addPropertyChangeListener(PropertyChangeListener listener);
  boolean isManager();
  void addObjectWithPassword(User user, String password) throws SQLException,
      RemoteException;
  void removeObject(Object obj) throws SQLException, RemoteException;
  ArrayList<Object> getList(String expectedType);
  User getLoggedInUser();
  String getUserPassword(String oldEmail) throws SQLException, RemoteException;
  void editObjectWithPassword(Object oldObj, Object newObj, String password)
      throws SQLException, RemoteException;
  Object getObject(Object obj, String expectedType) throws SQLException, RemoteException;
  void addObject(Object obj) throws SQLException, RemoteException;
}
