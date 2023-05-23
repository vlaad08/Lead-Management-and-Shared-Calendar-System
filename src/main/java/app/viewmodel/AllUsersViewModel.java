package app.viewmodel;

import app.model.Model;
import app.shared.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllUsersViewModel implements PropertyChangeListener
{

  private ObjectProperty<ObservableList<User>> users;
  private PropertyChangeSupport support;

  private final Model model;

  private SimpleStringProperty name;

  public AllUsersViewModel(Model model)
  {

    this.model = model;
    support = new PropertyChangeSupport(this);
    model.addPropertyChangeListener(this);

    name = new SimpleStringProperty();
    users = new SimpleObjectProperty<>();
    users.set(FXCollections.observableArrayList(model.getUsers()));

  }

  public void bindUserName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public boolean isManager()
  {
    return model.isManager();
  }

  public void bindUsers(ObjectProperty<ObservableList<User>> property)
  {
    property.bindBidirectional(users);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  public void addUser(User user) throws SQLException, RemoteException
  {
    model.addUser(user);
  }

  public ArrayList<User> getUsers() throws Exception
  {
    return model.getUsers();
  }

  public void createAddress(String street, String city, String country, String postalCode)
      throws SQLException, RemoteException
  {
    model.createAddress(street, city, country, postalCode);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadUser"))
    {
      ArrayList<User> list = model.getUsers();
      ObservableList<User> observableList= FXCollections.observableList(list);
      users.set(observableList);
      support.firePropertyChange("reloadUser", false, true);
    }
    if(evt.getPropertyName().equals("reloadLoggedInUser"))
    {
      name = new SimpleStringProperty(model.getLoggedInUserName());
    }
  }

  public void deleteUser(String email)
  {

  }

 public void updateUser(String email)
 {
  
 }

  public String getLoggedInUserName()
  {
    return model.getLoggedInUserName();
  }
}
