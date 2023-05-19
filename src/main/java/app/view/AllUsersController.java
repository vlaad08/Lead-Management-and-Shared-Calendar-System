package app.view;

import app.viewmodel.AllUsersViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AllUsersController
{
  @FXML private Button calendarButton;
  @FXML private Button tasksButton;
  @FXML private Button availableClientsButton;
  @FXML private Button meetingButton;

  @FXML private Button plansButton;
  @FXML private Button closeButton;
  @FXML private Button manageLeadsButton;
  @FXML private Button addUserButton;
  @FXML private VBox userVBox;


  private Region root;
  private ViewHandler viewHandler;
  private AllUsersViewModel allUsersViewModel;

  public void init(ViewHandler viewHandler, AllUsersViewModel allUsersViewModel, Region root){
    this.viewHandler = viewHandler;
    this.allUsersViewModel = allUsersViewModel;
    this.root = root;



    Draw.hoverButtonNavbar(calendarButton, plansButton, meetingButton, tasksButton, availableClientsButton, manageLeadsButton, closeButton);

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
        case "All Clients" -> viewHandler.openView("AllUsers");
        case "Manage leads" -> viewHandler.openView("Leads");
      }
    }
  }
//get Users
  public void addUser()
  {
    Draw.drawUserPopUp(userVBox, allUsersViewModel);
  }

}
