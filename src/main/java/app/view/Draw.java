package app.view;

import app.model.ConstraintChecker;
import app.shared.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;

import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Draw
{



  public static void drawMeetingPopUp(MeetingViewModel meetingViewModel)
      throws SQLException, RemoteException
  {
    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");
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
    leads.setItems(FXCollections.observableArrayList(meetingViewModel.getLeads()));
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

    ObservableList<UserTableRow> usersList = FXCollections.observableArrayList();

    TableView<UserTableRow> attendance = new TableView<>();
    attendance.setEditable(true);
    TableColumn<UserTableRow, String> firstName = new TableColumn<>("First Name");
    TableColumn<UserTableRow, String> lastName = new TableColumn<>("Last Name");
    TableColumn<UserTableRow, String> email = new TableColumn<>("Email");
    TableColumn<UserTableRow, String> attends = new TableColumn<>("Attends");

    attendance.getColumns().add(firstName);
    attendance.getColumns().add(lastName);
    attendance.getColumns().add(email);
    attendance.getColumns().add(attends);

    firstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    lastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    email.setCellValueFactory(cell -> cell.getValue().emailProperty());
    attends.setCellValueFactory(cell -> cell.getValue().attendsProperty());

    attends.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));


    ArrayList<User> users = meetingViewModel.getUsers();


    for (User user : users) {
      usersList.add(new UserTableRow(user));
    }

    attendance.setItems(usersList);




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
        ArrayList<String> emails = new ArrayList<>();
        for(UserTableRow row : attendance.getItems())
        {
          if(row.attendsProperty().getValue().equalsIgnoreCase("yes"))
          {
            emails.add(row.getEmail());
          }
        }
        createMeetingObject(meetingViewModel, titleTextField.getText(), leads.getValue(), datePicker, startTime.getText(), endTime.getText(), descrTextField.getText(), emails);
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

  public static void createMeetingObject(MeetingViewModel meetingViewModel, String title, Lead lead,
      DatePicker datePicker, String startTime, String endTime,
      String description, ArrayList<String> emails){

      Date date=Date.valueOf(datePicker.getValue());
      Platform.runLater(()->{

        try
        {
          meetingViewModel.addMeeting(title,description,date, Time.valueOf(LocalTime.parse(startTime)),Time.valueOf(LocalTime.parse(endTime)),lead.getEmail(),emails);
        }
        catch (SQLException | RemoteException e)
        {
          throw new RuntimeException(e);
        }

      });

  }

  private static VBox drawMeetingTile(MeetingViewModel meetingViewModel, String title, String leadEmail, DatePicker datePicker, String startTime, String endTime, String description) throws SQLException
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


    HBox lead = new HBox();
    Label leadLabel = new Label("Lead email: ");
    TextField leadTextField = new TextField(leadEmail);
    leadLabel.setTextFill(Paint.valueOf("White"));
    leadTextField.setBackground(null);
    leadTextField.setStyle("-fx-text-fill: white");
    leadTextField.setEditable(false);
    lead.setSpacing(20);
    lead.getChildren().addAll(leadLabel, leadTextField);

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

    meeting.getChildren().addAll(titleRow, lead, date, time1, time2, descr);

    meeting.setOnMouseClicked(event -> {

      try
      {
        drawManageMeetingPopUp(meetingViewModel, title, leadEmail, datePicker, startTime, endTime, description);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });

    return meeting;
  }

  public static void drawManageMeetingPopUp(MeetingViewModel meetingViewModel, String title, String leadEmail, DatePicker datePicker, String startTime, String endTime, String description)
      throws SQLException, RemoteException
  {

    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    //container
    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");
    parent.setPrefWidth(600);
    parent.setPrefHeight(400);
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

    HBox newTitle = new HBox();
    Label newTitleLabel = new Label("Title: ");
    TextField newTitleTextField = new TextField(title);
    newTitle.setSpacing(65);
    newTitle.setPadding(new Insets(20, 0, 0, 20));
    newTitle.getChildren().addAll(newTitleLabel, newTitleTextField);

    HBox lead=new HBox();
    Label leadLabel=new Label("Lead: ");

    ComboBox<Lead> leads=new ComboBox<>();

    ObservableList<Lead> leadsList = FXCollections.observableArrayList(meetingViewModel.getLeads());

    leads.setItems(leadsList);
    if(leadEmail != null)
    {
      for(Lead l : leadsList)
      {
        if(leadEmail.equals(l.getEmail()))
        {
          leads.setValue(l);
          break;
        }
      }
    }



    leads.setPrefWidth(150);
    lead.setSpacing(65);
    lead.setPadding(new Insets(20,0,0,20));
    lead.getChildren().addAll(leadLabel,leads);

    HBox dateTime = new HBox();
    dateTime.setPadding(new Insets(5));
    dateTime.setSpacing(20);
    Label startDate = new Label("Date:");
    startDate.setPrefWidth(75);
    DatePicker newDatePicker = new DatePicker(datePicker.getValue());
    newDatePicker.setPrefWidth(170);

    TextField newStartTime = new TextField(startTime);
    newStartTime.setPrefWidth(60);
    Label to = new Label("to: ");
    TextField newEndTime = new TextField(endTime);
    newEndTime.setPrefWidth(60);

    Button update = new Button("Update");
    update.setPrefWidth(60);
    update.setTextFill(Paint.valueOf("White"));
    update.setStyle("-fx-background-color:  #348e2f");

    dateTime.getChildren().add(startDate);
    dateTime.getChildren().add(newDatePicker);
    dateTime.getChildren().add(newStartTime);
    dateTime.getChildren().add(to);
    dateTime.getChildren().add(newEndTime);
    dateTime.getChildren().add(update);

    HBox descr = new HBox();
    descr.setSpacing(20);
    Label newDescription = new Label("Description:");
    newDescription.setPrefWidth(75);
    TextArea descrText = new TextArea(description);
    descrText.setPrefWidth(330);
    descrText.setPrefHeight(100);
    Button delete = new Button("Delete");
    delete.setPrefWidth(60);
    delete.setTextFill(Paint.valueOf("White"));
    delete.setStyle("-fx-background-color: #d93f3f");


    descr.getChildren().addAll(newDescription, descrText, delete);
    descr.setLayoutY(10);





    HBox employeeAttendance = new HBox();
    TableView<UserTableRow> attendance = new TableView<>();
    attendance.setEditable(true);
    TableColumn<UserTableRow, String> firstName = new TableColumn<>("First Name");
    TableColumn<UserTableRow, String> lastName = new TableColumn<>("Last Name");
    TableColumn<UserTableRow, String> email = new TableColumn<>("Email");
    TableColumn<UserTableRow, String> attends = new TableColumn<>("Attends");

    attendance.getColumns().add(firstName);
    attendance.getColumns().add(lastName);
    attendance.getColumns().add(email);
    attendance.getColumns().add(attends);

    firstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    lastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    email.setCellValueFactory(cell -> cell.getValue().emailProperty());
    attends.setCellValueFactory(cell -> cell.getValue().attendsProperty());

    attends.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));

    attendance.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    attendance.setPrefWidth(600);
    attendance.setPrefHeight(150);
    attendance.setPadding(Insets.EMPTY);



    Time start = Time.valueOf(LocalTime.parse(startTime));
    Time end = Time.valueOf(LocalTime.parse(endTime));


    Meeting m = new Meeting(title, description, Date.valueOf(datePicker.getValue()), start, end, null);
    ArrayList<User> users = meetingViewModel.getUsers();
    ObservableList<UserTableRow> userTableRows = FXCollections.observableArrayList();
    ArrayList<String> emails = meetingViewModel.getAttendance(m);


    for(User user : users)
    {
      UserTableRow userRow = new UserTableRow(user);
      userRow.setAttends("No");
      String userEmail = userRow.getEmail();
      for(String e : emails)
      {
        if(e.equalsIgnoreCase(userEmail))
        {
          userRow.setAttends("Yes");
        }
      }


      userTableRows.add(userRow);
    }


    attendance.setItems(userTableRows);



    employeeAttendance.getChildren().add(attendance);




    dateTime.setPadding(new Insets(20, 0, 0, 20));
    descr.setPadding(new Insets(20, 0, 10, 20));

    insert.addAll(topBar, newTitle,lead, dateTime, descr, employeeAttendance);

    update.setOnAction(event -> {

      if (ConstraintChecker.checkTime(newStartTime.getText(),newEndTime.getText()) && ConstraintChecker.checkDate(datePicker.getValue()))
      {
        ArrayList<String> emailsUpdated = new ArrayList<>();
        for(UserTableRow row : attendance.getItems())
        {
          if(row.attendsProperty().getValue().equalsIgnoreCase("yes"))
          {
            emailsUpdated.add(row.getEmail());
          }
        }
        try
        {
          Meeting oldMeeting = new Meeting(title, description, Date.valueOf(
              datePicker.getValue()), Time.valueOf(startTime), Time.valueOf(
              endTime), leadEmail);
          Meeting newMeeting = new Meeting(newTitleTextField.getText(), descrText.getText(), Date.valueOf(newDatePicker.getValue()), Time.valueOf(newStartTime.getText()), Time.valueOf(newEndTime.getText()), leads.getValue().getEmail());

          updateMeetingObject(meetingViewModel,oldMeeting,newMeeting, emailsUpdated);
          stage.close();
        }
        catch(NullPointerException e){
          e.fillInStackTrace();
          System.out.println("oldUsers are null");
        }

      }else
      {
        Alert A = new Alert(Alert.AlertType.ERROR);
        A.setContentText("Check time and date inputs");
        A.show();
      }

    });


    delete.setOnAction(event -> {
      Meeting meeting = new Meeting(title, description, Date.valueOf(
          datePicker.getValue()), Time.valueOf(startTime), Time.valueOf(
          endTime), leadEmail);
      try
      {
        confirmationToDeleteObject(meeting, meetingViewModel, stage);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });


    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

  public static void updateMeetingObject(MeetingViewModel meetingViewModel,Meeting oldMeeting, Meeting newMeeting,  ArrayList<String> emails)
  {

      Platform.runLater(()->{
        try
        {
          meetingViewModel.editMeeting(oldMeeting, newMeeting, emails);
        }
        catch (SQLException | RemoteException e)
        {
          throw new RuntimeException(e);
        }
      });

  }

  public static void drawTaskPopUp(TilePane tilePane, TasksViewModel tasksViewModel)
      throws SQLException, RemoteException
  {
    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");
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


    HBox businesses = new HBox();
    Label business = new Label("Business: ");
    ComboBox<Business> businessComboBox = new ComboBox<>();
    businessComboBox.setItems(FXCollections.observableArrayList(tasksViewModel.getBusinesses()));
    businesses.setPadding(new Insets(20, 0, 0, 20));
    businesses.setSpacing(40);


    businesses.getChildren().addAll(business, businessComboBox);


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

    HBox employeeChoice = new HBox();

    ObservableList<UserTableRow> usersList = FXCollections.observableArrayList();

    TableView<UserTableRow> assignTable = new TableView<>();
    assignTable.setEditable(true);
    TableColumn<UserTableRow, String> firstName = new TableColumn<>("First Name");
    TableColumn<UserTableRow, String> lastName = new TableColumn<>("Last Name");
    TableColumn<UserTableRow, String> email = new TableColumn<>("Email");
    TableColumn<UserTableRow, String> attends = new TableColumn<>("Assign");

    assignTable.getColumns().add(firstName);
    assignTable.getColumns().add(lastName);
    assignTable.getColumns().add(email);
    assignTable.getColumns().add(attends);

    firstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    lastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    email.setCellValueFactory(cell -> cell.getValue().emailProperty());
    attends.setCellValueFactory(cell -> cell.getValue().attendsProperty());

    attends.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));


    ArrayList<User> users = tasksViewModel.getUsers();


    for (User user : users) {
      usersList.add(new UserTableRow(user));
    }

    assignTable.setItems(usersList);




    assignTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    assignTable.setPrefWidth(600);
    assignTable.setPrefHeight(150);
    assignTable.setPadding(Insets.EMPTY);
    employeeChoice.getChildren().add(assignTable);



    dueDate.setPadding(new Insets(20, 0, 0, 20));
    descr.setPadding(new Insets(20, 0, 0, 20));
    status.setPadding(new Insets(20, 0, 10, 20));

    insert.addAll(topBar, title,  businesses,descr, dueDate,  status,employeeChoice);


    create.setOnAction(event -> {
      if (ConstraintChecker.checkFillOut(titleTextField))
      {
        if(ConstraintChecker.checkDate(datePicker.getValue()))
        {
          ArrayList<String> emails = new ArrayList<>();
          for(UserTableRow row : assignTable.getItems())
          {
            if(row.attendsProperty().getValue().equalsIgnoreCase("yes"))
            {
              emails.add(row.getEmail());
            }
          }
          createTaskObject(tilePane, tasksViewModel, titleTextField.getText(), descrTextField.getText(), datePicker, statuses.getValue(), businessComboBox.getValue(), emails);
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

  public static VBox drawTaskTile(TasksViewModel tasksViewModel, String title, String description, DatePicker datePicker, String status, int business_id)
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
      try
      {
        drawManageTaskPopUp(tasksViewModel,  title, description, datePicker.getValue(), status, business_id);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });



    return task;
  }

  public static void drawManageTaskPopUp(TasksViewModel tasksViewModel, String title, String description, LocalDate date, String status, int business_id)
      throws SQLException, RemoteException
  {

    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");
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

    HBox businesses = new HBox();
    Label business = new Label("Business: ");
    ComboBox<Business> businessComboBox = new ComboBox<>();
    businessComboBox.setItems(FXCollections.observableArrayList(tasksViewModel.getBusinesses()));

    for(Business b : tasksViewModel.getBusinesses())
    {
      if(b.getBusiness_id() == business_id)
      {
        businessComboBox.setValue(b);
        break;
      }
    }

    businesses.setPadding(new Insets(20, 0, 0, 20));
    businesses.setSpacing(40);


    businesses.getChildren().addAll(business, businessComboBox);

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
    Button delete = new Button("Delete");
    delete.setPrefWidth(60);
    delete.setTextFill(Paint.valueOf("White"));
    delete.setStyle("-fx-background-color: #d93f3f");

    dueDate.getChildren().add(dueDateLabel);
    dueDate.getChildren().add(datePicker);
    dueDate.getChildren().add(update);
    dueDate.getChildren().add(delete);


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




    HBox employeeChoice = new HBox();



    TableView<UserTableRow> assignTable = new TableView<>();
    assignTable.setEditable(true);
    TableColumn<UserTableRow, String> firstName = new TableColumn<>("First Name");
    TableColumn<UserTableRow, String> lastName = new TableColumn<>("Last Name");
    TableColumn<UserTableRow, String> email = new TableColumn<>("Email");
    TableColumn<UserTableRow, String> attends = new TableColumn<>("Assign");

    assignTable.getColumns().add(firstName);
    assignTable.getColumns().add(lastName);
    assignTable.getColumns().add(email);
    assignTable.getColumns().add(attends);

    firstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    lastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    email.setCellValueFactory(cell -> cell.getValue().emailProperty());
    attends.setCellValueFactory(cell -> cell.getValue().attendsProperty());

    attends.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));

    ObservableList<UserTableRow> usersList = FXCollections.observableArrayList();
    ArrayList<User> users = tasksViewModel.getUsers();

    Task t = new Task(title, description, Date.valueOf(date),
        status, business_id);
    ArrayList<String> emails = tasksViewModel.getAssignedUsers(t);


    for(User user : users)
    {
      UserTableRow userRow = new UserTableRow(user);
      userRow.setAttends("No");
      String userEmail = userRow.getEmail();
      for(String e : emails)
      {
        if(e.equalsIgnoreCase(userEmail))
        {
          userRow.setAttends("Yes");
        }
      }


      usersList.add(userRow);
    }

    assignTable.setItems(usersList);




    assignTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    assignTable.setPrefWidth(600);
    assignTable.setPrefHeight(150);
    assignTable.setPadding(Insets.EMPTY);
    employeeChoice.getChildren().add(assignTable);

    insert.addAll(topBar, titleBox, businesses, descr, dueDate,  statusBox, assignTable);


    update.setOnAction(event -> {
      if (ConstraintChecker.checkFillOut(titleTextField))
      {
        if(ConstraintChecker.checkDate(datePicker.getValue()))
        {
          ArrayList<String> emailsUpdated = new ArrayList<>();
          for(UserTableRow row : assignTable.getItems())
          {
            if(row.attendsProperty().getValue().equalsIgnoreCase("yes"))
            {
              emailsUpdated.add(row.getEmail());
            }
          }
          Task newTask = new Task(titleTextField.getText(), descrTextField.getText(), Date.valueOf(datePicker.getValue()), statuses.getValue(), 7456);
          Task oldTask = new Task(title, description, Date.valueOf(date),
              status, business_id);
          updateTaskObject(tasksViewModel, newTask, oldTask, emailsUpdated);
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
    delete.setOnAction(event -> {
      try
      {
        confirmationToDeleteObject(new Task(title, description, Date.valueOf(date),
            status, business_id), tasksViewModel, stage);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });

    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }

  public static void confirmationToDeleteObject(Object obj, Object viewModel, Stage primaryStage)
      throws SQLException, RemoteException
  {
    Stage stage = new Stage();
    VBox parent = new VBox();
    parent.setPadding(new Insets(20));
    Label label = new Label("Are you sure you want to delete this ");

    HBox buttons = new HBox();
    buttons.setSpacing(30);
    Button yesButton = new Button("Yes");
    Button noButton = new Button("No");
    yesButton.setStyle("-fx-background-color:  #348e2f");
    noButton.setStyle("-fx-background-color: #d93f3f");
    yesButton.setTextFill(Paint.valueOf("White"));
    noButton.setTextFill(Paint.valueOf("White"));
    yesButton.setPadding(new Insets(5));
    noButton.setPadding(new Insets(5));

    String text = label.getText();

    buttons.getChildren().addAll(yesButton, noButton);
    parent.getChildren().addAll(label, buttons);

    Scene scene = new Scene(parent);
    stage.setScene(scene);
    stage.show();

    if(obj instanceof Task && viewModel instanceof TasksViewModel)
    {

      label.setText(text + "task?");
      yesButton.setOnAction(event -> {
        try
        {
          primaryStage.close();
          stage.close();
          ((TasksViewModel) viewModel).removeTask((Task) obj);
        }
        catch (SQLException | RemoteException e)
        {
          throw new RuntimeException(e);
        }
      });
      noButton.setOnAction(event -> stage.close());
    }
    else if(obj instanceof Meeting && viewModel instanceof MeetingViewModel)
    {
      label.setText(text + "meeting?");
      yesButton.setOnAction(event -> {
        primaryStage.close();
        stage.close();
        try
        {
          ((MeetingViewModel) viewModel).removeMeeting((Meeting) obj);
        }
        catch (SQLException | RemoteException e)
        {
          throw new RuntimeException(e);
        }
      });
      noButton.setOnAction(event -> stage.close());
    }
    else if(obj instanceof Lead && viewModel instanceof LeadsViewModel)
    {
      label.setText(text + "lead?");
      yesButton.setOnAction(event -> {
        primaryStage.close();
        stage.close();
        try
        {
          ((LeadsViewModel) viewModel).removeLead((Lead) obj);
        }
        catch (SQLException | RemoteException e)
        {
          throw new RuntimeException(e);
        }
      });
      noButton.setOnAction(event -> stage.close());
    }
  }

   public static void createTaskObject(TilePane tilePane, TasksViewModel tasksViewModel, String title, String description, DatePicker dueDate, String status, Business business, ArrayList<String> emails){

    tilePane.getChildren().add(
        drawTaskTile(tasksViewModel, title,  description, dueDate, status, business.getBusiness_id()));
    Date date=Date.valueOf(dueDate.getValue());
    Platform.runLater(()->{
      try
      {
        tasksViewModel.addTask(title, description, date, status, business.getBusiness_id(), emails);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });

  }

  private static void updateTaskObject(TasksViewModel tasksViewModel, Task newTask, Task oldTask, ArrayList<String> emails)
  {
    Platform.runLater(()->{
      try
      {
        tasksViewModel.editTask(newTask, oldTask, emails);
      }
      catch (SQLException | RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });
  }

  public static void drawMeetings(TilePane tilePane, ObservableList<Meeting> meetings, MeetingViewModel meetingViewModel)
  {
    if(meetings != null)
    {
      for(Node node : tilePane.getChildren())
      {
        if(node instanceof VBox)
        {
          tilePane.getChildren().remove(node);
        }
      }

      for(Meeting meeting : meetings)
      {
        LocalDate date = meeting.getDate().toLocalDate();
        DatePicker datePicker=new DatePicker(date);
        String startTime=meeting.getStartTime().toString();
        String endTime=meeting.getEndTime().toString();
        Platform.runLater(()->{
          try
          {
            tilePane.getChildren().add(
                drawMeetingTile(meetingViewModel, meeting.getTitle(),meeting.getLeadEmail(),datePicker,startTime,endTime,
                    meeting.getDescription()));

          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });
      }
    }
  }

  public static void drawTasks(TilePane tilePane, TasksViewModel viewModel, ObservableList<Task> tasks)
  {
    if(tasks != null)
    {
      for(Node node : tilePane.getChildren())
      {
        if(node instanceof VBox)
        {
          Platform.runLater(()-> tilePane.getChildren().remove(node));
        }
      }

      for(Task task : tasks)
      {
        LocalDate date = task.getDate().toLocalDate();
        DatePicker datePicker=new DatePicker(date);
        Platform.runLater(()-> tilePane.getChildren().add(
            drawTaskTile(viewModel, task.getTitle(), task.getDescription(), datePicker,
                task.getStatus(), task.getBusiness_id())));
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

  public static void drawLeadPopUp(LeadsViewModel leadsViewModel)
      throws SQLException, RemoteException
  {
    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);



    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");

    parent.setPrefHeight(600);
    parent.setPrefWidth(800);
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

    HBox name = new HBox();
    Label firstNameLabel = new Label("First Name: ");
    firstNameLabel.setPrefWidth(65);
    Label middleNameLabel = new Label("Middle Name: ");
    TextField middleNameTextField = new TextField();
    middleNameTextField.setPromptText("Can be left empty");
    TextField firstNameTextField = new TextField();
    Label lastNameLabel = new Label("Last Name: ");
    TextField lastNameTextField = new TextField();
    name.setSpacing(20);
    name.setPadding(new Insets(20, 0, 0, 20));
    name.getChildren().addAll(firstNameLabel, firstNameTextField,middleNameLabel, middleNameTextField,lastNameLabel,lastNameTextField);

    HBox data1 = new HBox();

    Label emailLabel = new Label("Email: ");
    emailLabel.setPrefWidth(65);
    TextField emailField = new TextField();
    Label phoneLabel = new Label("Phone: ");
    phoneLabel.setPrefWidth(77);
    TextField phoneField = new TextField();
    phoneField.setText("");


    data1.getChildren().addAll(emailLabel,emailField,phoneLabel,phoneField);
    data1.setSpacing(20);
    data1.setPadding(new Insets(20,0,0,20));




    HBox businesses = new HBox();
    Label businessLabel = new Label("Business: ");
    businessLabel.setPrefWidth(65);
    ComboBox<Business> businessComboBox = new ComboBox<>();
    businessComboBox.setItems(FXCollections.observableArrayList(leadsViewModel.getBusinesses()));


    businesses.setSpacing(20);
    businesses.setPadding(new Insets(20,0,0,20));
    businesses.getChildren().addAll(businessLabel, businessComboBox);





    HBox workplace1 = new HBox();
    Label businessName = new Label("Business: ");
    businessName.setPrefWidth(65);
    TextField businessNameTextField = new TextField();
    Label titleLabel = new Label("Title: ");
    titleLabel.setPrefWidth(65);
    TextField titleTextField = new TextField();
    workplace1.setSpacing(20);
    workplace1.setPadding(new Insets(20, 0, 0, 20));
    workplace1.getChildren().addAll(businessName, businessNameTextField, titleLabel, titleTextField);


    HBox workplace2 = new HBox();
    Label businessAddress = new Label("Address: ");
    businessAddress.setPrefWidth(65);
    TextField businessAddressTextField = new TextField();
    Label businessCity = new Label("City: ");
    TextField businessCityTextField = new TextField();
    businessCity.setPrefWidth(65);
    workplace2.setSpacing(20);
    workplace2.setPadding(new Insets(20, 0, 0, 20));
    workplace2.getChildren().addAll(businessAddress, businessAddressTextField, businessCity, businessCityTextField);


    HBox workplace3 = new HBox();
    Label businessCountry = new Label("Country: ");
    TextField businessCountryTextField = new TextField();
    businessCountry.setPrefWidth(65);
    Label businessPostalCode = new Label("Postal Code: ");
    TextField businessPostalCodeTextField = new TextField();
    businessPostalCode.setPrefWidth(65);

    workplace3.setSpacing(20);
    workplace3.setPadding(new Insets(20, 0, 0, 20));
    workplace3.getChildren().addAll(businessCountry, businessCountryTextField, businessPostalCode, businessPostalCodeTextField);

    Button create = new Button("Create");
    create.setPrefWidth(60);
    create.setTextFill(Paint.valueOf("White"));
    create.setStyle("-fx-background-color:  #348e2f");

    businessComboBox.setOnAction(event -> {

    });

    insert.addAll(topBar, name, data1, businesses, workplace1, workplace2, workplace3, create);

    firstNameTextField.setPromptText("Required");
    lastNameTextField.setPromptText("Required");
    emailField.setPromptText("Required");
    phoneField.setPromptText("Required");
    titleTextField.setPromptText("Required");
    create.setOnAction(event -> {
      if (ConstraintChecker.checkFillOut(firstNameTextField) &&
          ConstraintChecker.checkFillOut(lastNameTextField)  &&
          ConstraintChecker.checkFillOut(emailField) &&
          ConstraintChecker.checkFillOut(phoneField) &&
          ConstraintChecker.checkFillOut(titleTextField))
      {

        if(businessComboBox.getValue() == null)
        {

            Platform.runLater(()->{
              try
              {
                leadsViewModel.createAddress(businessAddressTextField.getText(), businessCityTextField.getText(), businessCountryTextField.getText(), businessPostalCodeTextField.getText());
              }
              catch (SQLException | RemoteException e)
              {
                throw new RuntimeException(e);
              }
              try
              {
                leadsViewModel.createBusiness(businessNameTextField.getText(), businessAddressTextField.getText(), businessPostalCodeTextField.getText());
              }
              catch (SQLException | RemoteException e)
              {
                throw new RuntimeException(e);
              }

              Business b = new Business(businessNameTextField.getText(), businessAddressTextField.getText(), Integer.parseInt(businessPostalCodeTextField.getText()));

              int id;
              try
              {
                id = leadsViewModel.getBusinessId(b);
              }
              catch (SQLException | RemoteException e)
              {
                throw new RuntimeException(e);
              }
              createLeadObject(leadsViewModel, firstNameTextField.getText(), middleNameTextField.getText() ,lastNameTextField.getText(),emailField.getText(),phoneField.getText(),titleTextField.getText(), id, businessNameTextField.getText());

            });
          }
        else
        {
          createLeadObject(leadsViewModel, firstNameTextField.getText(),
              middleNameTextField.getText(), lastNameTextField.getText(), emailField.getText(), phoneField.getText(), titleTextField.getText(), businessComboBox.getValue().getBusiness_id(), businessComboBox.getValue().getName());
        }

        stage.close();
      }else
      {
        Alert A = new Alert(Alert.AlertType.ERROR);
        A.setContentText("Complete all required fields");
        A.show();
      }

    });



    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }

  public static void createLeadObject(LeadsViewModel leadsViewModel, String firstName, String middleName, String lastName, String email, String phone, String title, int businessID, String businessName ){

    Platform.runLater(()->{
      try
      {
        leadsViewModel.addLead(new Lead(firstName, middleName, lastName, email, phone, title, businessID, businessName));
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    });

  }

  public static HBox drawLeadTile(LeadsViewModel leadsViewModel, String firstName, String middleName, String lastName, String email, String businessName,  String title, String phone, int business_id)
  {
    HBox lead = new HBox();

    lead.setPadding(new Insets(10,10,10,50));
    lead.setPrefWidth(794);
    lead.setPrefHeight(60);
    lead.setStyle("-fx-background-color: #e2e0eb; -fx-background-radius: 15; -fx-border-radius: 15");

    Label nameLabel = new Label(firstName+" "+lastName);
    nameLabel.setTextFill(Paint.valueOf("White"));
    nameLabel.setPrefWidth(270);
    nameLabel.setPrefHeight(46);
    nameLabel.setStyle(" -fx-background-color: #544997; -fx-font: bold 16pt 'System'; -fx-background-radius: 5px;");
    nameLabel.setAlignment(Pos.CENTER);

    Label emailLabel = new Label(email);
    emailLabel.setPrefWidth(270);
    emailLabel.setPrefHeight(48);
    emailLabel.setFont(new Font(18));
    emailLabel.setAlignment(Pos.CENTER);
    emailLabel.setPadding(new Insets(0));

    Label businessLabel = new Label(businessName);
    businessLabel.setPrefWidth(270);
    businessLabel.setPrefHeight(48);
    businessLabel.setFont(new Font(18));
    businessLabel.setAlignment(Pos.CENTER);
    businessLabel.setPadding(new Insets(0));


    Label titleLabel = new Label(title);
    titleLabel.setPrefWidth(240);
    titleLabel.setPrefHeight(48);
    titleLabel.setFont(new Font(18));
    titleLabel.setAlignment(Pos.CENTER);


    lead.getChildren().addAll(nameLabel, emailLabel, businessLabel, titleLabel);

    lead.setOnMouseClicked(event -> {
      drawManageLeadPopUp(leadsViewModel, firstName, middleName, lastName, email, businessName, title, phone, business_id);
    });

    return lead;
  }

  private static void drawManageLeadPopUp(LeadsViewModel leadsViewModel, String firstName, String middleName, String lastName, String email,
      String businessName, String title, String phone, int business_id)
  {
    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");
    parent.setPrefHeight(500);
    parent.setPrefWidth(700);
    parent.setAlignment(Pos.TOP_LEFT);


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


    HBox names = new HBox();
    names.setAlignment(Pos.CENTER);
    Label firstNameLabel = new Label("First Name: ");
    TextField firstnameTextField = new TextField(firstName);
    Label middleNameLabel = new Label("Middle Name: ");
    TextField middleNameTextField = new TextField(middleName);
    Label lastNameLabel = new Label("Last Name: ");
    TextField lastNameTextField = new TextField(lastName);

    names.getChildren().addAll(firstNameLabel,firstnameTextField, middleNameLabel, middleNameTextField, lastNameLabel, lastNameTextField);

    HBox data = new HBox();
    data.setAlignment(Pos.CENTER);
    Label emailLabel = new Label("Email: ");
    TextField emailTextField = new TextField(email);
    Label phoneLabel = new Label("Phone: ");
    TextField phoneTextField = new TextField(phone);
    Label role= new Label("Role: ");
    TextField roleTextField = new TextField(title);

    data.getChildren().addAll(emailLabel, emailTextField, phoneLabel, phoneTextField, role, roleTextField);

    HBox buttons = new HBox();
    buttons.setAlignment(Pos.CENTER);
    Button update = new Button("Update");
    update.setPrefWidth(60);
    update.setTextFill(Paint.valueOf("White"));
    update.setStyle("-fx-background-color:  #348e2f");
    Button delete = new Button("Delete");
    delete.setPrefWidth(60);
    delete.setTextFill(Paint.valueOf("White"));
    delete.setStyle("-fx-background-color: #d93f3f");

    buttons.getChildren().addAll(update, delete);

    parent.getChildren().addAll(topBar, names, data, buttons);

    update.setOnAction(event -> {
      Lead oldLead = new Lead(firstName, middleName, lastName, email, phone, title, business_id, businessName);
      Lead newLead = new Lead(firstnameTextField.getText(), middleNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), phoneTextField.getText(), roleTextField.getText(),
          business_id, businessName);
      try
      {
        leadsViewModel.editLead(oldLead, newLead);
        stage.close();
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
      catch (RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });
    delete.setOnAction(event -> {
      Lead lead = new Lead(firstName, middleName, lastName, email, phone, title, business_id, businessName);
      try
      {
        confirmationToDeleteObject(lead, leadsViewModel, stage);
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
      catch (RemoteException e)
      {
        throw new RuntimeException(e);
      }
    });

    Scene scene = new Scene(parent);
    stage.setScene(scene);
    stage.show();
  }

  public static void drawLeads(VBox leadVBox, ObservableList<Lead> leads, LeadsViewModel leadsViewModel)
  {
    if(leads != null)
    {
      for(Node node : leadVBox.getChildren())
      {
        if(node instanceof HBox)
        {
          Platform.runLater(()-> leadVBox.getChildren().remove(node));
        }
      }

      for(Lead lead : leads)
      {
        Platform.runLater(()->
        {
          leadVBox.getChildren().add(drawLeadTile(leadsViewModel, lead.getFirstname(), lead.getMiddleName(), lead.getLastname(), lead.getEmail(), lead.getBusinessName(),lead.getTitle(), lead.getPhone(), lead.getBusiness_id()));
        });
      }
    }
  }

  public static void drawCalendarActivityPopUp(List<CalendarActivity> calendarActivities)
  {
    Stage stage = new Stage();
    stage.initStyle(StageStyle.UNDECORATED);

    VBox parent = new VBox();
    parent.setStyle("-fx-border-color: black; -fx-border-width: 1.5");
    parent.setPrefHeight(400);
    parent.setPrefWidth(600);
    parent.setAlignment(Pos.TOP_LEFT);


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



    parent.getChildren().add(topBar);

    for (CalendarActivity calendarActivity : calendarActivities)
    {
      HBox activity = new HBox();
      activity.setSpacing(20);
      activity.setPadding(new Insets(20));
      Text text = new Text();
      text.setFont(Font.font(15));
      if(calendarActivity.getMeeting() != null)
      {
        text.setText(calendarActivity.getMeeting().toString());
      }
      if(calendarActivity.getTask() != null)
      {
        text.setText(calendarActivity.getTask().toString());
      }

      activity.getChildren().add(text);
      parent.getChildren().add(activity);
    }






    Scene scene =new Scene(parent);
    stage.setScene(scene);
    stage.show();
  }
}
