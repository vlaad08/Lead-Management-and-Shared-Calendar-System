package app.model;

import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ModelListener
{
  void meetingAddedFromServer()
      throws SQLException, RemoteException;

  void taskAddedFromServer() throws SQLException, RemoteException;


  void leadAddedFromServer() throws SQLException, RemoteException;

  void userAddedFromServer() throws SQLException, RemoteException;
}
