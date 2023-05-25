package app.model;

import app.shared.Business;
import app.shared.User;

import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ModelLead
{
  void addPropertyChangeListener(PropertyChangeListener listener);
  User getLoggedInUser();
  void addObject(Object obj) throws SQLException, RemoteException;
  void removeObject(Object obj) throws SQLException, RemoteException;
  ArrayList<Object> getList(String expectedType);
  boolean isManager();
  int getBusinessId(Business business) throws SQLException, RemoteException;
  void editObject(Object oldObj, Object newObj) throws SQLException, RemoteException;
}
