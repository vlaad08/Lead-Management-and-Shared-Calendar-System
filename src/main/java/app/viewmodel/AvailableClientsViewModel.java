package app.viewmodel;

import app.model.Model;
import app.shared.Lead;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class AvailableClientsViewModel implements PropertyChangeListener
{
  private final Model model;
  private final PropertyChangeSupport support;
  private final ObjectProperty<ObservableList<Lead>> availableLeads;
  private SimpleStringProperty name;

  public AvailableClientsViewModel(Model model){
    this.model = model;
    support = new PropertyChangeSupport(this);
    model.addPropertyChangeListener(this);
    name = new SimpleStringProperty(model.getLoggedInUserName());
    availableLeads = new SimpleObjectProperty<>();
    availableLeads.set(FXCollections.observableArrayList(getAvailableLeads()));
  }

  public void bindUserName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public boolean isManager()
  {
    return model.isManager();
  }

  public ArrayList<Lead> getAvailableLeads(){
    ArrayList<Lead> available = new ArrayList<>();
    for(Lead element: model.getLeads()){
      if(element.getStatus().equals("Available")){
        available.add(element);
      }
    }
    return available;
  }

  public void bindLeads(ObjectProperty<ObservableList<Lead>> property)
  {
    property.bindBidirectional(availableLeads);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadLeads"))
    {
      ArrayList<Lead> list = getAvailableLeads();
      ObservableList<Lead> observableList= FXCollections.observableList(list);
      availableLeads.set(observableList);
      support.firePropertyChange("reloadLeads", false, true);
    }

    if(evt.getPropertyName().equals("reloadLoggedInUser"))
    {
      name = new SimpleStringProperty(model.getLoggedInUserName());
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

}
