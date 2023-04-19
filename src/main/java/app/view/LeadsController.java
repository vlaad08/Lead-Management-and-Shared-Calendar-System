package app.view;

import app.viewmodel.LeadsViewModel;
import app.viewmodel.MeetingViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class LeadsController
{
  @FXML private Button Calendar;
  @FXML private Button Tasks;
  @FXML private Button AvailableClients;
  @FXML private Button AllClients;
  @FXML private Button ManageMeeting;


  private Region root;
  private ViewHandler viewHandler;
  private LeadsViewModel leadsViewModel;

  public void init(ViewHandler viewHandler, LeadsViewModel leadsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.leadsViewModel = leadsViewModel;
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

  @FXML public void onAvailableClientsView(){
    viewHandler.openView("AvailableClients");
  }

  @FXML public void onAllClientsView(){
    viewHandler.openView("AllClients");
  }

  @FXML public void onManageMeetingView(){
    viewHandler.openView("Meeting");
  }
}
