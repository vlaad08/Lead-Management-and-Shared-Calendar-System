package app.viewmodel;

import app.model.Model;
import app.shared.Lead;
import app.shared.Meeting;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class LeadsViewModel implements PropertyChangeListener
{
  private Model model;
  private final PropertyChangeSupport support;
  private final ObjectProperty<ObservableList<Lead>> leads;

  public LeadsViewModel(Model model){
    this.model = model;
    model.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
    leads = new SimpleObjectProperty<>();
    leads.set(FXCollections.observableArrayList(getLeads()));
  }

  public void addLead(Lead lead) throws Exception{
    model.addLead(lead);
  }

  public ArrayList<Lead> getLeads(){
    try{
      return model.getLeads();
    }catch (Exception e){
     e.printStackTrace();
    }
    return null;
  }

  public void bind(ObjectProperty<ObservableList<Lead>> property){
    property.bindBidirectional(leads);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
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
