package app.viewmodel;

import app.model.Model;
import app.shared.Business;
import app.shared.Meeting;
import app.shared.Task;
import app.shared.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.Date;

import java.sql.SQLException;
import java.util.ArrayList;

public class TasksViewModel implements PropertyChangeListener
{
  private final Model model;

  private final ObjectProperty<ObservableList<Task>> tasks;

  private final PropertyChangeSupport support;

  public TasksViewModel(Model model){
    support = new PropertyChangeSupport(this);

    this.model = model;

    model.addPropertyChangeListener(this);

    tasks = new SimpleObjectProperty<>();
    tasks.set(FXCollections.observableArrayList(model.getTasks()));
  }

  public void bindTask(ObjectProperty<ObservableList<Task>> property)
  {
    property.bindBidirectional(tasks);
  }


  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  public void addTask(String title, String description, Date date, String status, int business_id, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    model.addTask(title, description, date, status, business_id, emails);
  }

  public void editTask(Task newTask, Task oldTask, ArrayList<String> emails)
      throws SQLException, RemoteException
  {
    model.editTask(newTask, oldTask, emails);
  }

  public ArrayList<Task> getTasks(){
   return model.getTasks();
  }

  public ArrayList<Business> getBusinesses()
      throws SQLException, RemoteException
  {
    return model.getBusinesses();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadTasks"))
    {
      ArrayList<Task> list = model.getTasks();
      ObservableList<Task> observableList= FXCollections.observableList(list);
      tasks.set(observableList);
      support.firePropertyChange("reloadTasks", false, true);
    }
  }

  public ArrayList<User> getUsers() throws SQLException, RemoteException
  {
    return model.getUsers();
  }

  public ArrayList<String> getAssignedUsers(Task task)
      throws SQLException, RemoteException
  {
    return model.getAssignedUsers(task);
  }

  public void removeTask(Task task) throws SQLException, RemoteException
  {
    model.removeTask(task);
  }
}
