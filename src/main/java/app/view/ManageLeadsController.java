package app.view;

import app.shared.Lead;
import app.shared.Meeting;
import app.viewmodel.LeadsViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

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
  private final ListView<Lead> leads = new ListView<>();

  public void init(ViewHandler viewHandler, LeadsViewModel leadsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.leadsViewModel = leadsViewModel;
    this.root = root;

    leadsViewModel.addPropertyChangeListener(this);

    //bs comes below
    hoverButtonNavbar(calendarButton);
    hoverButtonNavbar(availableClientsButton);
    hoverButtonNavbar(plansButton);
    hoverButtonNavbar(meetingButton);
    hoverButtonNavbar(tasksButton);
    hoverButtonNavbar(clientsButton);
    hoverButtonNavbar(closeButton);

    leadsViewModel.bind(leads.itemsProperty());
    Draw.drawLead(leadVBox, leadsViewModel,leads,0);
  }

  public void hoverButtonNavbar(Button b)
  {
    b.setOnMouseEntered(event -> {
      b.setStyle("-fx-background-color: #786FAC;");
    });
    b.setOnMouseExited(event -> {
      b.setStyle("-fx-background-color: none");
    });
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

  public void addLead(){
    Draw.drawLeadPopUp(leadVBox, leadsViewModel);
  }
  private void dorwMeetingObject(){

  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadLead"))
    {
      Platform.runLater(()->{
        Draw.drawLead(leadVBox, leadsViewModel, leads,0);
      });

    }
  }
}
