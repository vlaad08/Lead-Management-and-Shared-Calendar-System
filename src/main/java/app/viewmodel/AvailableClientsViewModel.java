package app.viewmodel;

import app.model.Model;
import app.shared.Lead;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class AvailableClientsViewModel implements PropertyChangeListener
{
  private Model model;
  private final PropertyChangeSupport support;
  private final ObjectProperty<ObservableList<Lead>> leads;


  public AvailableClientsViewModel(Model model){
    this.model = model;
    model.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
    leads = new SimpleObjectProperty<>();
    leads.set(FXCollections.observableArrayList(getAvailableLeads()));
  }

  public ArrayList<Lead> getAvailableLeads(){
    try{
      ArrayList<Lead> leads = model.getLeads();
      ArrayList<Lead> available = new ArrayList<>();
      for(Lead element: leads){
        if(element.status().equals("Available")){
          available.add(element);
        }
      }
      return available;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public void bind(ObjectProperty<ObservableList<Lead>> property){
    property.bindBidirectional(leads);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadLead"))
    {
      try{
        ArrayList<Lead> list = model.getLeads();
        ObservableList<Lead> observableList = FXCollections.observableList(list);
        leads.set(observableList);
        support.firePropertyChange("reloadLead", false, true);
      }catch (Exception e){
        e.printStackTrace();
      }

    }
  }
}
