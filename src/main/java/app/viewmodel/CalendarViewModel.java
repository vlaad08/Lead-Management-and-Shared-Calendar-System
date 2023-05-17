package app.viewmodel;

import app.model.Model;
import app.shared.Meeting;
import app.shared.Task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CalendarViewModel implements PropertyChangeListener
{
  private final Model model;

  private final PropertyChangeSupport support;

  public CalendarViewModel(Model model){
    this.model = model;
    model.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  public ArrayList<Meeting> getMeetings()
  {
    return model.getMeetings();
  }

  public ArrayList<Task> getTasks()
  {
    return model.getTasks();
  }

  public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings"))
    {
      support.firePropertyChange("reloadCalendar", false, true);
    }
    if(evt.getPropertyName().equals("reloadTasks"))
    {
      support.firePropertyChange("reloadCalendar", false, true);
    }
  }
}
