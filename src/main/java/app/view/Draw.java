package app.view;

import app.model.ConstraintChecker;
import app.model.User;
import app.shared.Lead;
import app.shared.Meeting;
import app.shared.Task;
import app.viewmodel.LeadsViewModel;
import app.viewmodel.MeetingViewModel;
import app.viewmodel.TasksViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Draw
{

  public static void drawMeetingPopUp(TilePane tilePane, MeetingViewModel meetingViewModel)
  {
    Stage stage = new Stage();

    VBox parent = new VBox();
    parent.setPrefHeight(400);
    parent.setPrefWidth(600);
    parent.setAlignment(Pos.TOP_LEFT);
    ObservableList<Node> insert = parent.getChildren();

    HBox topBar = new HBox();
    Button closeButton = new Button("X");
    closeButton.setOnAction(event -> stage.close());
    closeButton.setStyle("-fx-background-color: none");
    closeButton.setTextFill(Paint.valueOf("White"));
    hoverButtonNavbar(closeButton);
    closeButton.setPrefHeight(40);
    topBar.setPrefHeight(40);
    topBar.setAlignment(Pos.CENTER_RIGHT);
    topBar.setStyle("-fx-background-color:  #544997");
    topBar.getChildren().add(closeButton);

    HBox title = new HBox();
    Label titleLabel = new Label("Title: ");
    TextField titleTextField = new TextField();
    title.setSpacing(65);
    title.setPadding(new Insets(20, 0, 0, 20));
    title.getChildren().addAll(titleLabel, titleTextField);

    HBox lead=new HBox();
    Label leadLabel=new Label("Lead: ");
    ComboBox<Lead> leads=new ComboBox<>();
    leads.setPrefWidth(150);
    lead.setSpacing(65);
    lead.setPadding(new Insets(20,0,0,20));
    lead.getChildren().addAll(leadLabel,leads);

    HBox dateTime = new HBox();
    dateTime.setPadding(new Insets(5));
    dateTime.setSpacing(20);
    Label startDate = new Label("Date:");
    startDate.setPrefWidth(75);
    DatePicker datePicker = new DatePicker(LocalDate.now());
    datePicker.setPrefWidth(170);
    TextField startTime = new TextField(
        LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")));
    startTime.setPrefWidth(60);
    Label to = new Label("to: ");
    TextField endTime = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")));
    endTime.setPrefWidth(60);
    Button create = new Button("Create");
    create.setPrefWidth(60);
    create.setTextFill(Paint.valueOf("White"));
    create.setStyle("-fx-background-color:  #348e2f");

    dateTime.getChildren().add(startDate);
    dateTime.getChildren().add(datePicker);
    dateTime.getChildren().add(startTime);
    dateTime.getChildren().add(to);
    dateTime.getChildren().add(endTime);
    dateTime.getChildren().add(create);

    HBox descr = new HBox();
    descr.setSpacing(20);
    Label description = new Label("Description:");
    description.setPrefWidth(75);
    TextField descrTextField = new TextField();
    descrTextField.setAlignment(Pos.TOP_LEFT);
    descrTextField.setPrefWidth(330);
    descrTextField.setPrefHeight(100);
    descr.getChildren().add(description);
    descr.getChildren().add(descrTextField);
    descr.setLayoutY(10);
    HBox employeeAttendance = new HBox();
    TableView<User> attendance = new TableView<>();
    TableColumn<User, String> firstName = new TableColumn<>("First Name");
    TableColumn<User, String> lastName = new TableColumn<>("Last Name");
    TableColumn<User, String> attends = new TableColumn<>("Attends");

    firstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
    lastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
    attends.setCellFactory(ComboBoxTableCell.forTableColumn(""));
    attends.setCellValueFactory(new PropertyValueFactory<>("attends"));

    attendance.getColumns().setAll(firstName, lastName, attends);

    attendance.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    attendance.setPrefWidth(600);
    attendance.setPrefHeight(150);
    attendance.setPadding(Insets.EMPTY);
    employeeAttendance.getChildren().add(attendance);


    dateTime.setPadding(new Insets(20, 0, 0, 20));
    descr.setPadding(new Insets(20, 0, 10, 20));

    insert.addAll(topBar, title,lead, dateTime, descr, employeeAttendance);

    Platform.runLater(()-> create.setOnAction(event -> {
      if (ConstraintChecker.checkTime(startTime.getText(),endTime.getText()) && ConstraintChecker.checkDate(datePicker.getValue()))
      {
        createMeetingObject(tilePane, meetingViewModel, titleTextField.getText(),null, datePicker, startTime.getText(), endTime.getText(), descrTextField.getText(), attendance);
        stage.close();
      }else
      {
        Alert A = new Alert(Alert.AlertType.ERROR);
        A.setContentText("Check time and date inputs");
        A.show();
      }

    }));

    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }

  public static void createMeetingObject(TilePane tilePane,
      MeetingViewModel meetingViewModel, String title, ComboBox<Lead> lead,
      DatePicker datePicker, String startTime, String endTime,
      String description, TableView<User> users){
    try
    {
      tilePane.getChildren().add(
          drawMeetingTile(title, datePicker, startTime, endTime, description, users));
      Date date=Date.valueOf(datePicker.getValue());
      Platform.runLater(()->{
        try
        {
          meetingViewModel.addMeeting(title,description,date, Time.valueOf(LocalTime.parse(startTime)),Time.valueOf(LocalTime.parse(endTime)),null);
        }
        catch (SQLException | RemoteException e)
        {
          throw new RuntimeException(e);
        }
      });
    }
    catch (SQLException | NullPointerException e)
    {
      throw new RuntimeException(e);
    }
  }

  private static VBox drawMeetingTile(String title, DatePicker datePicker, String startTime, String endTime, String description, TableView<User> users) throws SQLException
  {
    VBox meeting = new VBox();

    meeting.setPadding(new Insets(10));

    meeting.setStyle("-fx-background-color: #544997; -fx-background-radius: 20px;");
    meeting.setPrefWidth(200);
    meeting.setPrefHeight(200);

    datePicker.setDisable(true);

    HBox titleRow = new HBox();
    Label titleLabel = new Label("Title: ");
    titleLabel.setTextFill(Paint.valueOf("White"));
    TextField titleTextField = new TextField(title);
    titleTextField.setBackground(null);
    titleTextField.setStyle("-fx-text-fill: white");
    titleTextField.setEditable(false);
    titleRow.setSpacing(20);

    HBox date = new HBox();
    Label dateLabel = new Label("Date: ");
    dateLabel.setTextFill(Paint.valueOf("White"));
    date.setSpacing(20);


    HBox time1 = new HBox();
    Label startTimeLabel = new Label("Start Time: ");
    startTimeLabel.setTextFill(Paint.valueOf("White"));
    TextField startTimeTextField = new TextField(startTime);
    startTimeTextField.setEditable(false);
    startTimeTextField.setBackground(null);
    startTimeTextField.setStyle("-fx-text-fill:white");
    time1.setSpacing(20);

    HBox time2 = new HBox();
    Label endTimeLabel = new Label("End Time : ");
    endTimeLabel.setTextFill(Paint.valueOf("White"));
    TextField endTimeTextField = new TextField(endTime);
    endTimeTextField.setEditable(false);
    endTimeTextField.setBackground(null);
    endTimeTextField.setStyle("-fx-text-fill: white");
    time2.setSpacing(20);


    HBox descr = new HBox();
    TextField descTextField = new TextField(description);
    descTextField.setEditable(false);
    descTextField.setBackground(null);
    descTextField.setStyle("-fx-text-fill: white");
    descr.setSpacing(20);

    titleRow.getChildren().addAll(titleLabel, titleTextField);
    date.getChildren().addAll(dateLabel, datePicker);
    time1.getChildren().addAll(startTimeLabel, startTimeTextField);
    time2.getChildren().addAll(endTimeLabel, endTimeTextField);
    descr.getChildren().add(descTextField);

    titleRow.setPadding(new Insets(10, 0, 10, 0));
    date.setPadding(new Insets(10, 0, 10, 0));
    time1.setPadding(new Insets(10, 0, 10, 0));
    time2.setPadding(new Insets(10, 0, 10, 0));
    descr.setPadding(new Insets(10, 0, 10, 0));

    meeting.getChildren().addAll(titleRow, date, time1, time2, descr);


    return meeting;
  }

  public static void drawTaskPopUp(TilePane tilePane, TasksViewModel tasksViewModel)
  {
    Stage stage = new Stage();

    VBox parent = new VBox();
    parent.setPrefHeight(400);
    parent.setPrefWidth(600);
    parent.setAlignment(Pos.TOP_LEFT);
    ObservableList<Node> insert = parent.getChildren();

    HBox topBar = new HBox();
    Button closeButton = new Button("X");
    closeButton.setOnAction(event -> stage.close());
    closeButton.setStyle("-fx-background-color: none");
    closeButton.setTextFill(Paint.valueOf("White"));
    hoverButtonNavbar(closeButton);
    closeButton.setPrefHeight(40);
    topBar.setPrefHeight(40);
    topBar.setAlignment(Pos.CENTER_RIGHT);
    topBar.setStyle("-fx-background-color:  #544997");
    topBar.getChildren().add(closeButton);

    HBox title = new HBox();
    Label titleLabel = new Label("Title: ");
    TextField titleTextField = new TextField();
    titleTextField.setText("");
    title.setSpacing(65);
    title.setPadding(new Insets(20, 0, 0, 20));
    title.getChildren().addAll(titleLabel, titleTextField);

    HBox descr = new HBox();
    descr.setSpacing(20);
    Label description = new Label("Description:");
    description.setPrefWidth(75);
    TextField descrTextField = new TextField();
    descrTextField.setAlignment(Pos.TOP_LEFT);
    descrTextField.setPrefWidth(330);
    descrTextField.setPrefHeight(100);
    descr.getChildren().add(description);
    descr.getChildren().add(descrTextField);
    descr.setLayoutY(10);


    HBox dueDate = new HBox();
    dueDate.setPadding(new Insets(5));
    dueDate.setSpacing(20);
    Label dueDateLabel = new Label("Due date:");
    dueDateLabel.setPrefWidth(75);
    DatePicker datePicker = new DatePicker(LocalDate.now());
    datePicker.setPrefWidth(170);
    Button create = new Button("Create");
    create.setPrefWidth(60);
    create.setTextFill(Paint.valueOf("White"));
    create.setStyle("-fx-background-color:  #348e2f");

    dueDate.getChildren().add(dueDateLabel);
    dueDate.getChildren().add(datePicker);
    dueDate.getChildren().add(create);


    HBox status = new HBox();
    Label statusLabel = new Label("Status");
    ComboBox<String> statuses = new ComboBox<>();
    ObservableList<String> statusChoices = FXCollections.observableArrayList("To do", "In Progress", "Ready", "Complete", "Incomplete");
    statuses.getItems().setAll(statusChoices);
    statuses.setValue("To do");
    status.setSpacing(60);
    status.getChildren().addAll(statusLabel, statuses);


    dueDate.setPadding(new Insets(20, 0, 0, 20));
    descr.setPadding(new Insets(20, 0, 0, 20));
    status.setPadding(new Insets(20, 0, 10, 20));

    insert.addAll(topBar, title, descr, dueDate,  status);


    create.setOnAction(event -> {
      if (ConstraintChecker.checkFillout(titleTextField))
      {
        if(ConstraintChecker.checkDate(datePicker.getValue()))
        {
          createTaskObject(tilePane, tasksViewModel, titleTextField.getText(), descrTextField.getText(), datePicker, statuses.getValue());
          stage.close();
        }
        else
        {
          Alert A = new Alert(Alert.AlertType.ERROR);
          A.setContentText("Check date input");
          A.show();
        }
      }else
      {
        Alert A = new Alert(Alert.AlertType.ERROR);
        A.setContentText("Title must not be empty");
        A.show();
      }

    });


    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }



  public static VBox drawTaskTile(TilePane tilePane, TasksViewModel tasksViewModel, String title, String description, DatePicker datePicker, String status)
  {
    VBox task = new VBox();

    task.setPadding(new Insets(10));

    task.setStyle("-fx-background-color: #544997; -fx-background-radius: 20px;");
    task.setPrefWidth(200);
    task.setPrefHeight(200);

    datePicker.setDisable(true);
    ComboBox<String> statusChoice = new ComboBox<>();
    statusChoice.setDisable(true);
    ObservableList<String> statusChoices = FXCollections.observableArrayList("To do", "In Progress", "Ready", "Complete", "Incomplete");
    statusChoice.getItems().setAll(statusChoices);
    statusChoice.setValue(status);


    HBox titleRow = new HBox();
    Label titleLabel = new Label("Title: ");
    titleLabel.setTextFill(Paint.valueOf("White"));
    TextField titleTextField = new TextField(title);
    titleTextField.setBackground(null);
    titleTextField.setStyle("-fx-text-fill: white");
    titleTextField.setEditable(false);
    titleRow.setSpacing(20);

    HBox descr = new HBox();
    TextField descTextField = new TextField(description);
    descTextField.setEditable(false);
    descTextField.setBackground(null);
    descTextField.setStyle("-fx-text-fill: white");
    descr.setSpacing(20);

    HBox date = new HBox();
    Label dateLabel = new Label("Date: ");
    dateLabel.setTextFill(Paint.valueOf("White"));
    date.setSpacing(20);

    HBox statusBox = new HBox();
    Label statusLabel = new Label("Status");
    statusLabel.setTextFill(Paint.valueOf("White"));
    statusBox.setSpacing(20);




    titleRow.getChildren().addAll(titleLabel, titleTextField);
    date.getChildren().addAll(dateLabel, datePicker);
    descr.getChildren().add(descTextField);
    statusBox.getChildren().addAll(statusLabel, statusChoice);

    titleRow.setPadding(new Insets(10, 0, 10, 0));
    descr.setPadding(new Insets(10, 0, 10, 0));
    date.setPadding(new Insets(10, 0, 10, 0));
    statusBox.setPadding(new Insets(10, 0, 10, 0));


    task.getChildren().addAll(titleRow, descr ,date, statusBox);

    task.setOnMouseClicked(event -> {
      drawManageTaskPopUp( tilePane, tasksViewModel,  title, description, datePicker.getValue(), status);
    });


    return task;
  }

  public static void drawManageTaskPopUp(TilePane tilePane, TasksViewModel tasksViewModel, String title, String description, LocalDate date, String status)
  {

    Stage stage = new Stage();

    VBox parent = new VBox();
    parent.setPrefHeight(400);
    parent.setPrefWidth(600);
    parent.setAlignment(Pos.TOP_LEFT);
    ObservableList<Node> insert = parent.getChildren();

    HBox topBar = new HBox();
    Button closeButton = new Button("X");
    closeButton.setOnAction(event -> stage.close());
    closeButton.setStyle("-fx-background-color: none");
    closeButton.setTextFill(Paint.valueOf("White"));
    hoverButtonNavbar(closeButton);
    closeButton.setPrefHeight(40);
    topBar.setPrefHeight(40);
    topBar.setAlignment(Pos.CENTER_RIGHT);
    topBar.setStyle("-fx-background-color:  #544997");
    topBar.getChildren().add(closeButton);

    HBox titleBox = new HBox();
    Label titleLabel = new Label("Title: ");
    TextField titleTextField = new TextField(title);
    titleBox.setSpacing(65);
    titleBox.setPadding(new Insets(20, 0, 0, 20));
    titleBox.getChildren().addAll(titleLabel, titleTextField);

    HBox descr = new HBox();
    descr.setSpacing(20);
    Label descriptionLabel = new Label("Description:");
    descriptionLabel.setPrefWidth(75);
    TextField descrTextField = new TextField(description);
    descrTextField.setAlignment(Pos.TOP_LEFT);
    descrTextField.setPrefWidth(330);
    descrTextField.setPrefHeight(100);
    descr.getChildren().add(descriptionLabel);
    descr.getChildren().add(descrTextField);
    descr.setLayoutY(10);


    HBox dueDate = new HBox();
    dueDate.setPadding(new Insets(5));
    dueDate.setSpacing(20);
    Label dueDateLabel = new Label("Due date:");
    dueDateLabel.setPrefWidth(75);
    DatePicker datePicker = new DatePicker(LocalDate.now());
    datePicker.setPrefWidth(170);
    Button update = new Button("Update");
    update.setPrefWidth(60);
    update.setTextFill(Paint.valueOf("White"));
    update.setStyle("-fx-background-color:  #348e2f");

    dueDate.getChildren().add(dueDateLabel);
    dueDate.getChildren().add(datePicker);
    dueDate.getChildren().add(update);


    HBox statusBox = new HBox();
    Label statusLabel = new Label("Status");
    ComboBox<String> statuses = new ComboBox<>();
    ObservableList<String> statusChoices = FXCollections.observableArrayList("To do", "In progress", "Ready", "Complete", "Incomplete");
    statuses.getItems().setAll(statusChoices);
    statuses.setValue(status);
    statusBox.setSpacing(60);
    statusBox.getChildren().addAll(statusLabel, statuses);


    dueDate.setPadding(new Insets(20, 0, 0, 20));
    descr.setPadding(new Insets(20, 0, 0, 20));
    statusBox.setPadding(new Insets(20, 0, 10, 20));

    insert.addAll(topBar, titleBox, descr, dueDate,  statusBox);


    update.setOnAction(event -> {
      if (ConstraintChecker.checkFillout(titleTextField))
      {
        if(ConstraintChecker.checkDate(datePicker.getValue()))
        {
          Task newTask = new Task(titleTextField.getText(), descrTextField.getText(), Date.valueOf(datePicker.getValue()), statuses.getValue(), 7456);
          Task oldTask = new Task(title, description, Date.valueOf(date),
              status, 7456);
          updateTaskObject(tasksViewModel, newTask, oldTask);
          stage.close();
        }
        else
        {
          Alert A = new Alert(Alert.AlertType.ERROR);
          A.setContentText("Check date input");
          A.show();
        }
      }else
      {
        Alert A = new Alert(Alert.AlertType.ERROR);
        A.setContentText("Title must not be empty");
        A.show();
      }

    });


    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }

  public static void createTaskObject(TilePane tilePane, TasksViewModel tasksViewModel, String title, String description, DatePicker dueDate, String status){

    tilePane.getChildren().add(
        drawTaskTile(tilePane, tasksViewModel, title,  description, dueDate, status));
    Date date=Date.valueOf(dueDate.getValue());
    Platform.runLater(()->{
      try
      {
        tasksViewModel.addTask(title, description, date, status, 7456);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });

  }

  private static void updateTaskObject(TasksViewModel tasksViewModel, Task newTask, Task oldTask)
  {
    Platform.runLater(()->{
      try
      {
        tasksViewModel.editTask(newTask, oldTask);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });
  }

  public static void drawMeetings(TilePane tilePane, ListView<Meeting> meetings)
  {
    if(meetings.getItems() != null)
    {
      for(Node node : tilePane.getChildren())
      {
        if(node instanceof VBox)
        {
          Platform.runLater(()-> tilePane.getChildren().remove(node));
        }
      }

      for(Meeting meeting : meetings.getItems())
      {
        LocalDate date = meeting.date().toLocalDate();
        DatePicker datePicker=new DatePicker(date);
        String startTime=meeting.startTime().toString();
        String endTime=meeting.endTime().toString();
        Platform.runLater(()->{
          try
          {
            tilePane.getChildren().add(
                drawMeetingTile(meeting.title(),datePicker,startTime,endTime,
                    meeting.description(), null));

          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
      }
    }
  }

  public static void drawTasks(TilePane tilePane, TasksViewModel viewModel, ListView<Task> tasks)
  {
    if(tasks.getItems() != null)
    {
      for(Node node : tilePane.getChildren())
      {
        if(node instanceof VBox)
        {
          Platform.runLater(()-> tilePane.getChildren().remove(node));
        }
      }

      for(Task task : tasks.getItems())
      {
        LocalDate date = task.date().toLocalDate();
        DatePicker datePicker=new DatePicker(date);
        Platform.runLater(()->{
          tilePane.getChildren().add(
              drawTaskTile(tilePane, viewModel, task.title(), task.description(), datePicker,
                  task.status()));

        });
      }
    }
  }



  public static void hoverButtonNavbar(Button... b)
  {
    for(Button button : b)
    {
      button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #786FAC;"));
      button.setOnMouseExited(event -> button.setStyle("-fx-background-color: none"));
    }
  }

  //All the Code that is bellow is Experimental
  //is used for Manage Leads

  public static void drawLeadPopUp(TilePane tilePane, LeadsViewModel leadsViewModel)
  {
    Stage stage = new Stage();

    VBox parent = new VBox();
    parent.setPrefHeight(400);
    parent.setPrefWidth(600);
    parent.setAlignment(Pos.TOP_LEFT);
    ObservableList<Node> insert = parent.getChildren();

    HBox topBar = new HBox();
    topBar.setPrefHeight(40);
    topBar.setAlignment(Pos.CENTER_RIGHT);
    topBar.setStyle("-fx-background-color:  #544997");

    HBox name = new HBox();
    Label firstNameLabel = new Label("First Name: ");
    firstNameLabel.setPrefWidth(65);
    TextField firstNameTextField = new TextField();
    firstNameTextField.setText("");
    Label middleNameLabel = new Label("Last Name: ");
    TextField middleNameTextField = new TextField();
    middleNameTextField.setText("");
    name.setSpacing(20);
    name.setPadding(new Insets(20, 0, 0, 20));
    name.getChildren().addAll(firstNameLabel, firstNameTextField,middleNameLabel,middleNameTextField);

    HBox email = new HBox();
    Label emailLabel = new Label("Email: ");
    emailLabel.setPrefWidth(65);
    TextField emailField = new TextField();
    emailField.setText("");
    email.setSpacing(20);
    email.setPadding(new Insets(20,0,0,20));
    email.getChildren().addAll(emailLabel,emailField);

    HBox phone = new HBox();
    Label phoneLabel = new Label("Phone: ");
    phoneLabel.setPrefWidth(65);
    TextField phoneField = new TextField();
    emailField.setText("");
    phone.setSpacing(20);
    phone.setPadding(new Insets(20,0,0,20));
    phone.getChildren().addAll(phoneLabel,phoneField);

    HBox title = new HBox();
    Label titleLabel = new Label("Title: ");
    titleLabel.setPrefWidth(65);
    TextField titleTextField = new TextField();
    titleTextField.setText("");
    title.setSpacing(20);
    title.setPadding(new Insets(20, 0, 0, 20));
    title.getChildren().addAll(titleLabel, titleTextField);

    HBox businessID = new HBox();
    businessID.setPadding(new Insets(5));
    businessID.setSpacing(20);
    Label businessLabel = new Label("Business ID:");
    businessLabel.setPrefWidth(65);
    TextField businessField = new TextField();
    businessField.setText("");

    Button create = new Button("Create");
    create.setPrefWidth(60);
    create.setTextFill(Paint.valueOf("White"));
    create.setStyle("-fx-background-color:  #348e2f");

    businessID.getChildren().add(businessLabel);
    businessID.getChildren().add(businessField);
    businessID.getChildren().add(create);

    businessID.setPadding(new Insets(20, 0, 0, 20));

    insert.addAll(topBar, name,email, phone, title,businessID);


    create.setOnAction(event -> {
      if (ConstraintChecker.checkFillout(firstNameTextField) &&
          ConstraintChecker.checkFillout(middleNameTextField))
      {

          //createTaskObject(tilePane, tasksViewModel, titleTextField.getText(), descrTextField.getText(), datePicker, statuses.getValue());
          stage.close();
        {
          Alert A = new Alert(Alert.AlertType.ERROR);
          A.setContentText("Check date input");
          A.show();
        }
      }else
      {
        Alert A = new Alert(Alert.AlertType.ERROR);
        A.setContentText("Title must not be empty");
        A.show();
      }

    });


    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }




}




