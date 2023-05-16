package app.viewmodel;

import app.model.Model;
import app.shared.Business;
import app.shared.Lead;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LeadsViewModel
{
  private Model model;

  public LeadsViewModel(Model model){
    this.model = model;
  }

  public void addLead(Lead lead) throws Exception{
    model.addLead(lead);
  }

  public ArrayList<Lead> getLeads() throws Exception{
    return model.getLeads();
  }

  public ArrayList<Business> getBusinesses()
      throws SQLException, RemoteException
  {
    return model.getBusinesses();
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
}
