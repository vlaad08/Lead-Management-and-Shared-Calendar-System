package app.view;

import app.shared.Task;
import app.viewmodel.TasksViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskController
{
  private FlowPane calendar;
  @FXML private Button plansButton;
  @FXML private  Button meetingButton;
  @FXML private  Button leadButton;
  @FXML private  Button availableButton;
  @FXML private  Button clientsButton;
  @FXML private  Button manageLeadsButton;
  @FXML private  Button closeButton;

  @FXML private  Button AddTask;

  @FXML private BorderPane parent;
  private Region root;
  private ViewHandler viewHandler;
  private TasksViewModel tasksViewModel;

  //private ArrayList<Task> taskList;
  private List<Task> taskList = new ArrayList<>();

  ObservableList<Task> observableTaskList = FXCollections.observableArrayList();

  public void init(ViewHandler viewHandler, TasksViewModel tasksViewModel, Region root){
    this.viewHandler = viewHandler;
    this.tasksViewModel = tasksViewModel;
    this.root = root;
    ArrayList<Task> taskListView = new ArrayList<Task>();

    //bs comes below


    drawTiles();
    //drawTilesSecondTry();
  }


  public void drawTiles(){
    HBox container = new HBox();

    VBox tile0 = new VBox();
    TextArea title0 = new TextArea("Task title");
    TextArea description0 = new TextArea("Task description");
    ComboBox employees0 = new ComboBox<>();
    Text assignees0 = new Text("JP.Morgan, Vlad Putin Nita");

    tile0.getChildren().add(title0);           title0.setPrefSize(50,20);
    tile0.getChildren().add(description0);     description0.setPrefSize(100,20);
    tile0.getChildren().add(employees0);       employees0.setPrefSize(75,20);
    tile0.getChildren().add(assignees0);

    container.getChildren().add(0,tile0);
    parent.setCenter(container);
    //parent.getChildren().add(container);

    tile0.minHeight(50);
    tile0.minWidth(50);
    title0.maxHeight(50);
    tile0.maxWidth(50);

    container.minHeight(300);
    container.minWidth(300);



    /*HBox tile = new HBox();
    TextArea taskName = new TextArea("suck kuk");
    tile.minHeight(200);
    tile.minWidth(300);
    tile.getChildren().add(taskName);
    parent.getChildren().add(tile);
    parent.setCenter(tile);*/
  }

  public void drawTilesSecondTry(){
    double calendarWidth = calendar.getPrefWidth();
    double calendarHeight = calendar.getPrefHeight();
    double strokeWidth = 1;
    double spacingH = calendar.getHgap();
    double spacingV = calendar.getVgap();
    //rest of calendar shit

  }

  public void onAddTask()
  {
    Stage popup = new Stage();

    // Task Name
    Text taskNameText = new Text("Task Name:");
    TextArea taskNameTextArea = new TextArea();
    taskNameTextArea.setPrefSize(200, 20);

    // Description
    Text descriptionText = new Text("Description:");
    TextArea descriptionTextArea = new TextArea();
    descriptionTextArea.setPrefSize(200, 100);

    // Start Date
    Text startDateText = new Text("Start Date:");
    DatePicker startDatePicker = new DatePicker();

    // End Date
    Text endDateText = new Text("End Date:");
    DatePicker endDatePicker = new DatePicker();

    // Employees
    Text employeesText = new Text("Employees:");
    ComboBox<String> employeesComboBox = new ComboBox<>();
    employeesComboBox.setPrefSize(200, 20);
    employeesComboBox.getItems().addAll("Employee 1", "Employee 2", "Employee 3");

    // Button  WHOLE OF THIS SHOULD BE DIFF METHOD AND BIND IN SCENE BUILDER
    Button addButton = new Button("Add");
    addButton.setOnAction(event -> {
      // Create a new task with the values from the popup fields
      String taskName = taskNameTextArea.getText();
      String description = descriptionTextArea.getText();
      String employee = employeesComboBox.getValue();
      LocalDate startDate = startDatePicker.getValue();
      LocalDate endDate = endDatePicker.getValue();
      Task newTask = new Task(taskName, description, startDate, endDate);

      // Add the new task to the taskList
      taskList.add(newTask);

      // Update the taskListView
      //taskList.setItems(taskList);
      //observableTaskList.addAll(taskList);
      //taskList.setItems(observableTaskList);

      observableTaskList.addAll(taskList);     // MAKE THE FOR LOOP OS DISPLAY GENYO 
      //taskListView.setItems(observableTaskList);
      System.out.println(taskList);
      System.out.println(observableTaskList);
      // Close the popup
      popup.close();
    });

    // Layout
    VBox layout = new VBox();
    layout.getChildren().addAll(taskNameText, taskNameTextArea, descriptionText, descriptionTextArea, startDateText, startDatePicker, endDateText, endDatePicker, employeesText, employeesComboBox, addButton);
    Scene popupScene = new Scene(layout);
    popup.setScene(popupScene);
    popup.show();
  }

  /*public void addTaskFinal(){

    // Create a new task with the values from the popup fields
    String taskName = taskNameTextArea.getText();
    String description = descriptionTextArea.getText();
    String employee = employeesComboBox.getValue();
    LocalDate startDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();
    Task newTask = new Task(taskName, description, startDate, endDate);

  }*/




  public void hoverButtonNavbar (Button b)
    {
      b.setOnMouseEntered(event -> {
        b.setStyle("-fx-background-color: #786FAC;");
      });
      b.setOnMouseExited(event -> {
        b.setStyle("-fx-background-color: none");
      });
    }

    public void onCloseButton () {
    viewHandler.close();
  }

    public Region getRoot () {
    return root;
  }

    public void reset () {
  } //why not

    @FXML public void changeView (ActionEvent e)
    {
      if (e.getSource().getClass() == Button.class)
      {
        Button b = (Button) e.getSource();

        switch (b.getText())
        {
          case "Calendar", "Plans" -> viewHandler.openView("Calendar");
          case "Manage meeting" -> viewHandler.openView("Meeting");
          case "Manage task" -> viewHandler.openView("Task");
          case "Lead", "Available Clients" -> viewHandler.openView("AvailableClients");
          case "All Clients" -> viewHandler.openView("AllClients");
          case "Manage leads" -> viewHandler.openView("Leads");
        }
      }
    }
  }
