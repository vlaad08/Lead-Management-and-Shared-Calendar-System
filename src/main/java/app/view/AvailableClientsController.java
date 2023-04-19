package app.view;

import app.viewmodel.AvailableClientsViewModel;
import app.viewmodel.MeetingViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class AvailableClientsController
{
  @FXML private Button Calendar;
  @FXML private Button Tasks;
  @FXML private Button ManageMeeting;
  @FXML private Button AllClients;
  @FXML private Button Leads;


  private Region root;
  private ViewHandler viewHandler;
  private AvailableClientsViewModel availableClientsViewModel;

  public void init(ViewHandler viewHandler, AvailableClientsViewModel availableClientsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.availableClientsViewModel = availableClientsViewModel;
    this.root = root;

    //bs comes below
  }

  public Region getRoot()
  {
    return root;
  }

  public void  reset(){} //why not

  @FXML public void onCalendarView(){
    viewHandler.openView("Calendar");
  }
  @FXML public void onTasksView(){
    viewHandler.openView("Tasks");
  }

  @FXML public void onManageMeetingView(){
    viewHandler.openView("Meeting");
  }

  @FXML public void onAllClientsView(){
    viewHandler.openView("AllClients");
  }

  @FXML public void onLeadsView(){
    viewHandler.openView("Leads");
  }
}
