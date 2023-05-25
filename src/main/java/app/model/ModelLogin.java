package app.model;

import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ModelLogin
{
  boolean logIn(String email, String password)
      throws SQLException, RemoteException;
}
