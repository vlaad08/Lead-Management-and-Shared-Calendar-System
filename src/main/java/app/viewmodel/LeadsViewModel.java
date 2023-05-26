package app.viewmodel;

import app.model.Model;
import app.model.ModelLead;
import app.shared.Address;
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
  private final ModelLead model;

  private final ObjectProperty<ObservableList<Lead>> leads;

  private final PropertyChangeSupport support;
  private SimpleStringProperty name;

  public LeadsViewModel(ModelLead model){
    this.model = model;
    support = new PropertyChangeSupport(this);
    model.addPropertyChangeListener(this);

    name = new SimpleStringProperty(model.getLoggedInUser().getFirstName());
    leads = new SimpleObjectProperty<>();
    leads.set(FXCollections.observableArrayList(getLeads()));
  }

  public void bindUserName(StringProperty property)
  {
    property.bindBidirectional(name);
  }

  public void addLead(Lead lead) throws SQLException, RemoteException
  {
    model.addObject(lead);
  }

  public void removeLead(Lead lead) throws SQLException, RemoteException
  {
    model.removeObject(lead);
  }

  public ArrayList<Lead> getLeads()
  {
    ArrayList<Lead> leads1 = new ArrayList<>();
    ArrayList<Object> objects = model.getList("leads");


    for(Object obj : objects)
    {
      if(obj instanceof Lead)
      {
        leads1.add((Lead) obj);
      }
    }
    return leads1;
  }

  public ArrayList<Business> getBusinesses()
      throws SQLException, RemoteException
  {
    ArrayList<Business> businesses = new ArrayList<>();
    ArrayList<Object> objects = model.getList("businesses");

    for(Object obj : objects)
    {
      if(obj instanceof Business)
      {
        businesses.add((Business) obj);
      }
    }
    return businesses;
  }

  public boolean isManager()
  {
    return model.isManager();
  }

  public void bindLeads(ObjectProperty<ObservableList<Lead>> property)
  {
    property.bindBidirectional(leads);
  }

  public void createAddress(String street, String city, String country, int postalCode)
      throws SQLException, RemoteException
  {
    Address address = new Address(street, city, country, postalCode);

    model.addObject(address);
  }

  public void createBusiness(String businessName, String street, int postalCode)
      throws SQLException, RemoteException
  {
    Business business = new Business(businessName,street,postalCode);

    model.addObject(business);
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
      ArrayList<Lead> list = getLeads();
      ObservableList<Lead> observableList= FXCollections.observableList(list);
      leads.set(observableList);
      support.firePropertyChange("reloadLeads", false, true);
    }

    if(evt.getPropertyName().equals("reloadLoggedInUser"))
    {
      name = new SimpleStringProperty(model.getLoggedInUser().getFirstName());
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  public void editLead(Lead oldLead, Lead newLead)
      throws SQLException, RemoteException
  {
    model.editObject(oldLead,newLead);
  }
}
