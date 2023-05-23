package app.shared;

import dk.via.remote.observer.RemotePropertyChangeListener;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public interface Communicator extends Remote
{

  void addListener(RemotePropertyChangeListener<String> listener) throws
      RemoteException;

  int getBusinessId(Business business) throws SQLException, RemoteException;
  boolean logIn(String email, String password) throws SQLException, RemoteException;

  User getUserByEmail(String email) throws SQLException, RemoteException;

  void addObject(Object obj) throws SQLException, RemoteException;

  void addObjectWithPassword(Object obj, String password) throws SQLException, RemoteException;

  void addObject(Object obj, ArrayList<String> emails) throws SQLException, RemoteException;
  ArrayList<Object> getList(String expectedType) throws SQLException,
      RemoteException;

  ArrayList<String> getListOfEmployees(Object obj) throws SQLException, RemoteException;
  ArrayList<Object> getListOfAvailableEmployees(Date date, Time startTime, Time endTime) throws
      SQLException, RemoteException;

  void removeObject(Object obj) throws SQLException, RemoteException;

  ArrayList<Object> getListByUser(User user, String expectedType) throws SQLException, RemoteException;

  void editObject(Object oldObj, Object newObj) throws SQLException, RemoteException;

  void editObjectWithList(Object oldObj, Object newObj, ArrayList<String> emails) throws
      SQLException, RemoteException;
  String getUserPassword(String oldEmail) throws SQLException, RemoteException;
  void editObjectWithPassword(Object oldObj, Object newObj, String password)
      throws SQLException, RemoteException;
}
