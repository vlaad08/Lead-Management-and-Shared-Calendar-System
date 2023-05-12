package app.viewmodel;

import app.model.Model;
import app.shared.Meeting;
import app.shared.Task;
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


  public void addTask(String title, String description, Date date, String status, int business_id)
      throws SQLException, RemoteException
  {
    model.addTask(title, description, date, status, business_id);
  }

  public ArrayList<Task> getTasks(){
   return model.getTasks();
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
}
