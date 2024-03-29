package app.view;

import app.shared.Task;
import app.viewmodel.TasksViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class  TaskController implements PropertyChangeListener
{
  public Button addButton;
  public StackPane addRectangle;
  @FXML  public Label nameLabel;
  @FXML private Button plansButton;
  @FXML private  Button meetingButton;
  @FXML private  Button leadButton;
  @FXML private  Button availableButton;
  @FXML private  Button clientsButton;
  @FXML private  Button manageLeadsButton;
  @FXML private  Button closeButton;
  @FXML private TilePane tilePane;
  private Region root;
  private ViewHandler viewHandler;
  private TasksViewModel tasksViewModel;

  private final ObjectProperty<ObservableList<Task>> tasks = new SimpleObjectProperty<>();

  public void init(ViewHandler viewHandler, TasksViewModel tasksViewModel, Region root){
    this.viewHandler = viewHandler;
    this.tasksViewModel = tasksViewModel;
    this.root = root;

    tilePane.setHgap(40);
    tilePane.setVgap(50);
    tilePane.setPrefTileWidth(300);
    tilePane.setPrefColumns(3);
    tilePane.setPrefRows(1);
    tilePane.setTileAlignment(Pos.CENTER_LEFT);


    tasksViewModel.bindTask(tasks);

    tasksViewModel.addPropertyChangeListener(this);

    tasksViewModel.bindUserName(nameLabel.textProperty());

    //bs comes below
    Draw.hoverButtonNavbar(plansButton, meetingButton, leadButton, availableButton, clientsButton, manageLeadsButton, closeButton);

    Draw.drawTasks(tilePane, tasksViewModel, tasks.get());
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

  public void addTask() throws SQLException, RemoteException
  {
    if(tasksViewModel.isManager())
    {
      Draw.drawTaskPopUp(tilePane, tasksViewModel);
    }
    else
    {
      Alert info = new Alert(Alert.AlertType.INFORMATION);
      info.setContentText("Only a manager can add a task");
      info.show();
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadTasks"))
    {
      Platform.runLater(()->
      {

        Draw.drawTasks(tilePane, tasksViewModel, tasks.get());
      });
    }
  }
}
