package app.viewmodel;

import app.model.Model;
import app.model.ModelLogin;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class LoginViewModel
{
  private final ModelLogin model;

  public LoginViewModel(ModelLogin model)
  {
    this.model = model;
  }


  public boolean logIn(String email, String password)
      throws SQLException, RemoteException
  {
    return model.logIn(email, password);
  }
}
