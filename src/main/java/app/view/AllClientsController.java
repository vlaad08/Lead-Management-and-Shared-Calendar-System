package app.view;

import app.viewmodel.AllClientsViewModel;
import app.viewmodel.MeetingViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class AllClientsController
{
  @FXML private Button Calendar;
  @FXML private Button Tasks;
  @FXML private Button AvailableClients;
  @FXML private Button ManageMeeting;
  @FXML private Button Leads;


  private Region root;
  private ViewHandler viewHandler;
  private AllClientsViewModel allClientsViewModel;

  public void init(ViewHandler viewHandler, AllClientsViewModel allClientsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.allClientsViewModel = allClientsViewModel;
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

  @FXML public void onManageMeetingView(){
    viewHandler.openView("Meeting");
  }

  @FXML public void onLeadsView(){
    viewHandler.openView("Leads");
  }
}
