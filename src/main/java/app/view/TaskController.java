package app.view;

import app.viewmodel.TasksViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TaskController
{
  @FXML private Button plansButton;
  @FXML private  Button meetingButton;
  @FXML private  Button leadButton;
  @FXML private  Button availableButton;
  @FXML private  Button clientsButton;
  @FXML private  Button manageLeadsButton;
  @FXML private  Button closeButton;


  private Region root;
  private ViewHandler viewHandler;
  private TasksViewModel tasksViewModel;
  @FXML private VBox taskList;

  public void init(ViewHandler viewHandler, TasksViewModel tasksViewModel, Region root){
    this.viewHandler = viewHandler;
    this.tasksViewModel = tasksViewModel;
    this.root = root;

    //bs comes below
    hoverButtonNavbar(plansButton);
    hoverButtonNavbar(meetingButton);
    hoverButtonNavbar(leadButton);
    hoverButtonNavbar(availableButton);
    hoverButtonNavbar(clientsButton);
    hoverButtonNavbar(manageLeadsButton);
    hoverButtonNavbar(closeButton);

    Node[] nodes = new Node[3];
    for(int i=0; i<nodes.length; i++){
      try{
        nodes[i] = (Node) FXMLLoader.load(getClass().getResource("/app/TaskObject.fxml"));
        taskList.getChildren().add(nodes[i]);
      }catch (IOException e){
        e.printStackTrace();
      }
    }
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
}
