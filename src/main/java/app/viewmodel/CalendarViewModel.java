package app.viewmodel;

import app.model.Model;
import app.model.ModelCalendar;
import app.shared.Meeting;
import app.shared.Task;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CalendarViewModel implements PropertyChangeListener
{
  private final ModelCalendar model;

  private final PropertyChangeSupport support;
  private SimpleStringProperty name;

  public CalendarViewModel(ModelCalendar model){
    this.model = model;
    model.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
    name = new SimpleStringProperty(model.getLoggedInUser().getFirstName());
  }

  public void bindUserName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public boolean isManager()
  {
    return model.isManager();
  }

  public ArrayList<Meeting> getMeetings()
  {
    ArrayList<Meeting> meetings = new ArrayList<>();
    ArrayList<Object> objects = model.getList("meetings");

    for(Object obj : objects)
    {
      if(obj instanceof Meeting)
      {
        meetings.add((Meeting) obj);
      }
    }
    return meetings;
  }



  public ArrayList<Task> getTasks()
  {
    ArrayList<Task> taskArrayList = new ArrayList<>();
    ArrayList<Object> objects = model.getList("tasks");

    for(Object obj : objects)
    {
      if(obj instanceof Task)
      {
        taskArrayList.add((Task) obj);
      }
    }
    return taskArrayList;
  }

  public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings") || evt.getPropertyName().equals("reloadTasks"))
    {
      support.firePropertyChange("reloadCalendar", false, true);
    }
    if(evt.getPropertyName().equals("reloadLoggedInUser"))
    {
      name = new SimpleStringProperty(model.getLoggedInUser().getLastName());
    }
  }
}
