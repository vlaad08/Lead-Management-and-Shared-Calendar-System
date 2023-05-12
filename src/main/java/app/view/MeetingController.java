package app.view;


import app.model.ConstraintChecker;
import app.model.User;
import app.shared.Lead;
import app.shared.Meeting;
import app.viewmodel.MeetingViewModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.time.format.DateTimeFormatter;


public class MeetingController implements PropertyChangeListener
{

  private Region root;
  private ViewHandler viewHandler;
  private MeetingViewModel meetingViewModel;

  @FXML private Button plansButton;
  @FXML private  Button taskButton;
  @FXML private  Button leadButton;
  @FXML private  Button availableButton;
  @FXML private  Button clientsButton;
  @FXML private  Button manageLeadsButton;
  @FXML private  Button closeButton;
  @FXML private TilePane tilePane;
  @FXML private Button addButton;
  @FXML private StackPane addRectangle;


  private final ListView<Meeting> meetings = new ListView<>();

  public void init(ViewHandler viewHandler, MeetingViewModel meetingViewModel, Region root)
      throws SQLException
  {

    this.viewHandler = viewHandler;
    this.meetingViewModel = meetingViewModel;
    this.root = root;

    meetingViewModel.addPropertyChangeListener(this);



    //bs comes below
    hoverButtonNavbar(plansButton);
    hoverButtonNavbar(taskButton);
    hoverButtonNavbar(leadButton);
    hoverButtonNavbar(availableButton);
    hoverButtonNavbar(clientsButton);
    hoverButtonNavbar(manageLeadsButton);
    hoverButtonNavbar(closeButton);

    tilePane.setHgap(40);
    tilePane.setVgap(50);
    tilePane.setPrefTileWidth(300);
    tilePane.setPrefColumns(3);
    tilePane.setPrefRows(1);
    tilePane.setTileAlignment(Pos.CENTER_LEFT);


    meetingViewModel.bindMeetings(meetings.itemsProperty());


    drawExistingMeetings();
  }

  public void drawExistingMeetings() throws SQLException
  {
    for(Node node : tilePane.getChildren())
    {
      if(node instanceof VBox)
      {
        Platform.runLater(()->{
          tilePane.getChildren().remove(node);
        });
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
              createMeetingTile(meeting.title(),datePicker,startTime,endTime,
                  meeting.description(), null));

        }
        catch (SQLException e)
        {
          throw new RuntimeException(e);
        }
      });
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

  public void  reset() throws SQLException
  {
  } //why not

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


  public void addMeeting(){
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
    closeButton.setPrefHeight(40);
    hoverButtonNavbar(closeButton);
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
    TextField startTime = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm")));
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

    Platform.runLater(()->{
    create.setOnAction(event -> {
    if (ConstraintChecker.checkDateAndTime(datePicker.getValue(),startTime.getText(),endTime.getText()))
    {
      createMeetingObject(titleTextField.getText(),null, datePicker, startTime.getText(), endTime.getText(), descrTextField.getText(), attendance);
      stage.close();
    }else
    {
      Alert A = new Alert(Alert.AlertType.ERROR);
      A.setContentText("Check time and date inputs");
      A.show();
    }

  });
    });

    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }


  public void createMeetingObject(String title,ComboBox<Lead> lead, DatePicker datePicker, String startTime, String endTime, String description, TableView users){
    try
    {
      tilePane.getChildren().add(
          createMeetingTile(title, datePicker, startTime, endTime, description, users));
      Date date=Date.valueOf(datePicker.getValue());
      Platform.runLater(()->{
        try
        {
          meetingViewModel.addMeeting(title,description,date,Time.valueOf(LocalTime.parse(startTime)),Time.valueOf(LocalTime.parse(endTime)),null);
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

  public void removeMeeting(){
    //meetingViewModel.removeMeeting(meeting);
  }

  public void editMeeting(){

  }

  public VBox createMeetingTile(String title, DatePicker datePicker, String startTime, String endTime, String description, TableView users) throws SQLException
  {
    VBox meeting = new VBox();

    meeting.setPadding(new Insets(10));

    meeting.setStyle("-fx-background-color: #544997; -fx-background-radius: 20px;");
    meeting.setPrefWidth(200);
    meeting.setPrefHeight(200);


    String chosenTitle = title;
    DatePicker chosenDate = datePicker;
    chosenDate.setDisable(true);
    String chosenStartTime = startTime;
    String chosenEndTime = endTime;
    String chosenDescription = description;
    TableView chosenAttend = users;


    HBox titleRow = new HBox();
    Label titleLabel = new Label("Title: ");
    titleLabel.setTextFill(Paint.valueOf("White"));
    TextField titleTextField = new TextField(chosenTitle);
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
    TextField startTimeTextField = new TextField(chosenStartTime);
    startTimeTextField.setEditable(false);
    startTimeTextField.setBackground(null);
    startTimeTextField.setStyle("-fx-text-fill:white");
    time1.setSpacing(20);

    HBox time2 = new HBox();
    Label endTimeLabel = new Label("End Time : ");
    endTimeLabel.setTextFill(Paint.valueOf("White"));
    TextField endTimeTextField = new TextField(chosenEndTime);
    endTimeTextField.setEditable(false);
    endTimeTextField.setBackground(null);
    endTimeTextField.setStyle("-fx-text-fill: white");
    time2.setSpacing(20);


    HBox descr = new HBox();
    TextField descTextField = new TextField(chosenDescription);
    descTextField.setEditable(false);
    descTextField.setBackground(null);
    descTextField.setStyle("-fx-text-fill: white");
    descr.setSpacing(20);

    titleRow.getChildren().addAll(titleLabel, titleTextField);
    date.getChildren().addAll(dateLabel, chosenDate);
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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings"))
    {
        Platform.runLater(()->{
          try
          {
            drawExistingMeetings();
          }
          catch (SQLException e)
          {
            throw new RuntimeException(e);
          }
        });

    }
  }
}

