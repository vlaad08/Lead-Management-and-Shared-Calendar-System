package app.view;

import app.viewmodel.AvailableClientsViewModel;
import app.viewmodel.MeetingViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class AvailableClientsController
{



  @FXML private Button calendarButton;
  @FXML private Button tasksButton;
  @FXML private Button clientsButton;
  @FXML private Button meetingButton;
  @FXML private Button plansButton;
  @FXML private Button closeButton;
  @FXML private Button manageLeadsButton;

  private Region root;
  private ViewHandler viewHandler;
  private AvailableClientsViewModel availableClientsViewModel;

  public void init(ViewHandler viewHandler, AvailableClientsViewModel availableClientsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.availableClientsViewModel = availableClientsViewModel;
    this.root = root;


    Draw.hoverButtonNavbar(calendarButton, plansButton, meetingButton, tasksButton, clientsButton, manageLeadsButton, closeButton);

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
        case "Calendar", "Plans" -> viewHandler.openView("Calendar");
        case "Manage meeting" -> viewHandler.openView("Meeting");
        case "Manage task" -> viewHandler.openView("Task");
        case "Lead", "Available Clients" -> viewHandler.openView("AvailableClients");
        case "All Users" -> viewHandler.openView("AllUsers");
        case "Manage leads" -> viewHandler.openView("Leads");
      }
    }
  }
}
