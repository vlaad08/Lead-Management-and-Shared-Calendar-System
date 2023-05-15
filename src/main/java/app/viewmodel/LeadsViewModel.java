package app.viewmodel;

import app.model.Model;
import app.shared.Lead;

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

}
