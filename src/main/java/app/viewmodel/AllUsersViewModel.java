package app.viewmodel;

import app.model.Model;
import app.shared.Address;
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

  private final ObjectProperty<ObservableList<User>> users;
  private final PropertyChangeSupport support;

  private final Model model;

  private SimpleStringProperty name;

  public AllUsersViewModel(Model model)
  {

    this.model = model;
    support = new PropertyChangeSupport(this);
    model.addPropertyChangeListener(this);

    name = new SimpleStringProperty();
    users = new SimpleObjectProperty<>();




    users.set(FXCollections.observableArrayList(getUsers()));

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

  public void addUser(User user, String password) throws SQLException, RemoteException
  {
    model.addObjectWithPassword(user, password);
  }

  public void removeUser(String email) throws SQLException, RemoteException
  {
    model.removeObject(email);
  }

  public ArrayList<User> getUsers()
  {
    ArrayList<User> userArrayList = new ArrayList<>();
    ArrayList<Object> objects = model.getList("users");

    for(Object obj : objects)
    {
      if(obj instanceof User)
      {
        userArrayList.add((User) obj);
      }
    }
    return userArrayList;
  }

  public void createAddress(String street, String city, String country, int postalCode)
      throws SQLException, RemoteException
  {
    Address address = new Address(street, city, country, postalCode);
    model.addObject(address);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadUser"))
    {
      ArrayList<User> list = getUsers();
      ObservableList<User> observableList= FXCollections.observableList(list);
      users.set(observableList);
      support.firePropertyChange("reloadUser", false, true);
    }
    if(evt.getPropertyName().equals("reloadLoggedInUser"))
    {
      name = new SimpleStringProperty(model.getLoggedInUser().getFirstName());
    }
  }

  public String getUserPassword(String oldEmail)
      throws SQLException, RemoteException
  {
    return model.getUserPassword(oldEmail);
  }

  public User getLoggedInUser()
  {
    return model.getLoggedInUser();
  }

  public void editUser(User oldUser, User newUser, String password)
      throws SQLException, RemoteException
  {
    model.editObjectWithPassword(oldUser,newUser,password);
  }
}
