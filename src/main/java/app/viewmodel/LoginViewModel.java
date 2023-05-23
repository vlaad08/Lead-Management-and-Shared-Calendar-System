package app.viewmodel;

import app.model.Model;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class LoginViewModel
{
  private final Model model;

  public LoginViewModel(Model model)
  {
    this.model = model;
  }


  public boolean logIn(String email, String password)
      throws SQLException, RemoteException
  {
    return model.logIn(email, password);
  }
}
