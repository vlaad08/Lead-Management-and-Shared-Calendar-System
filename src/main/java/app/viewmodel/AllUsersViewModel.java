package app.viewmodel;

import app.model.Model;
import app.shared.User;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllUsersViewModel
{
  private Model model;

  public AllUsersViewModel(Model model)
  {
    this.model = model;
  }

  public void addUser(User user) throws SQLException, RemoteException
  {
    model.addUser(user);
  }

  public ArrayList<User> getUsers() throws Exception
  {
    return model.getUsers();
  }



}
