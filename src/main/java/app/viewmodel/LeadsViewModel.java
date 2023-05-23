package app.viewmodel;

import app.model.Model;
import app.shared.Business;
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
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LeadsViewModel implements PropertyChangeListener
{
  private final Model model;

  private final ObjectProperty<ObservableList<Lead>> leads;

  private final PropertyChangeSupport support;
  private SimpleStringProperty name;

  public LeadsViewModel(Model model){
    this.model = model;
    support = new PropertyChangeSupport(this);
    model.addPropertyChangeListener(this);

    name = new SimpleStringProperty(model.getLoggedInUserName());
    leads = new SimpleObjectProperty<>();
    leads.set(FXCollections.observableArrayList(getLeads()));
  }

  public void bindUserName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public void addLead(Lead lead) throws Exception{
    model.createLead(lead);
  }

  public void removeLead(Lead lead) throws SQLException, RemoteException
  {
    model.removeLead(lead);
  }

  public ArrayList<Lead> getLeads()
  {
    return model.getLeads();
  }

  public ArrayList<Business> getBusinesses()
      throws SQLException, RemoteException
  {
    return model.getBusinesses();
  }

  public boolean isManager()
  {
    return model.isManager();
  }

  public void bindLeads(ObjectProperty<ObservableList<Lead>> property)
  {
    property.bindBidirectional(leads);
  }

  public void createAddress(String street, String city, String country, String postalCode)
      throws SQLException, RemoteException
  {
    model.createAddress(street, city, country, postalCode);
  }

  public void createBusiness(String businessName, String street, String postalCode)
      throws SQLException, RemoteException
  {
    model.createBusiness(businessName,street,postalCode);
  }

  public int getBusinessId(Business business)
      throws SQLException, RemoteException
  {
    return model.getBusinessId(business);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadLeads"))
    {
      ArrayList<Lead> list = model.getLeads();
      ObservableList<Lead> observableList= FXCollections.observableList(list);
      leads.set(observableList);
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

  public void editLead(Lead oldLead, Lead newLead)
      throws SQLException, RemoteException
  {
    model.editLead(oldLead, newLead);
  }
}
