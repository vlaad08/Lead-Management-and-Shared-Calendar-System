package app.view;

import app.viewmodel.TasksViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class TasksController
{
  @FXML private Button Calendar;
  @FXML private Button ManageMeeting;
  @FXML private Button AvailableClients;
  @FXML private Button AllClients;
  @FXML private Button Leads;


  private Region root;
  private ViewHandler viewHandler;
  private TasksViewModel tasksViewModel;

  public void init(ViewHandler viewHandler, TasksViewModel tasksViewModel, Region root){
    this.viewHandler = viewHandler;
    this.tasksViewModel = tasksViewModel;
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
  @FXML public void onManageMeetingView(){
    viewHandler.openView("Meeting");
  }

  @FXML public void onAvailableClientsView(){
    viewHandler.openView("AvailableClients");
  }

  @FXML public void onAllClientsView(){
    viewHandler.openView("AllClients");
  }

  @FXML public void onLeadsView(){
    viewHandler.openView("Leads");
  }
}
