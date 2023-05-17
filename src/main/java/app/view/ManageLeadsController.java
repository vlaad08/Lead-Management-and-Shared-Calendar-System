package app.view;

import app.shared.Lead;
import app.viewmodel.LeadsViewModel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class ManageLeadsController implements PropertyChangeListener
{
  @FXML private Button calendarButton;
  @FXML private Button tasksButton;
  @FXML private Button clientsButton;
  @FXML private Button availableClientsButton;
  @FXML private Button meetingButton;
  @FXML private Button plansButton;
  @FXML private Button closeButton;
  @FXML private TilePane tilePane;

  //Experimental Code
  @FXML private VBox leadVBox;
  //



  private Region root;
  private ViewHandler viewHandler;
  private LeadsViewModel leadsViewModel;

  private ObjectProperty<ObservableList<Lead>> leads = new SimpleObjectProperty<>();

  public void init(ViewHandler viewHandler, LeadsViewModel leadsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.leadsViewModel = leadsViewModel;
    this.root = root;


    Draw.hoverButtonNavbar(calendarButton, availableClientsButton, plansButton, meetingButton, tasksButton, clientsButton, closeButton);

    leadsViewModel.bindLeads(leads);
    //Experimental Code

    //Close of experimental code
    Draw.drawLeads(leadVBox, leads.get(), leadsViewModel);
  }


  public void onCloseButton()
  {
    viewHandler.close();
  }

  public Region getRoot()
  {
    return root;
  }

  public void  reset(){} //why not

  @FXML public void changeView(ActionEvent e)
  {
    if(e.getSource().getClass() == Button.class)
    {
      Button b = (Button) e.getSource();

      switch (b.getText())
      {
        case "Calendar", "Plans" ->
            viewHandler.openView("Calendar");
        case "Manage meeting" -> viewHandler.openView("Meeting");
        case "Manage task" -> viewHandler.openView("Task");
        case "Lead", "Available Clients" ->
            viewHandler.openView("AvailableClients");
        case "All Clients" -> viewHandler.openView("AllClients");
        case "Manage leads" -> viewHandler.openView("Leads");
      }
    }
  }

  public void addLead() throws SQLException, RemoteException
  {
    Draw.drawLeadPopUp(leadsViewModel);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadLeads"))
    {
      Platform.runLater(()->
      {
        Draw.drawLeads(leadVBox, leads.get(), leadsViewModel);
      });
    }
  }
}
