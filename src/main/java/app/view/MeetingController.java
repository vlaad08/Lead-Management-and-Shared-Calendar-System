package app.view;


import app.model.User;
import app.viewmodel.MeetingViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

public class MeetingController
{
  @FXML private Button Calendar;
  @FXML private Button Tasks;
  @FXML private Button AvailableClients;
  @FXML private Button AllClients;
  @FXML private Button Leads;


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


  public void init(ViewHandler viewHandler, MeetingViewModel meetingViewModel, Region root){
    this.viewHandler = viewHandler;
    this.meetingViewModel = meetingViewModel;
    this.root = root;

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
    tilePane.setPrefTileWidth(200);
    tilePane.setPrefColumns(3);
    tilePane.setPrefRows(1);
    tilePane.setTileAlignment(Pos.CENTER_LEFT);
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


  public void addMeeting(){
    Stage stage = new Stage();

    VBox parent = new VBox();
    parent.setEffect(new DropShadow(20, Color.BLACK));
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

    HBox dateTime = new HBox();
    dateTime.setPadding(new Insets(5));
    dateTime.setSpacing(20);

    Label startDate = new Label("Start date:");
    startDate.setPrefWidth(75);

    DatePicker datePicker = new DatePicker(LocalDate.now());
    datePicker.setPrefWidth(170);
    TextField startTime = new TextField(LocalTime.now().format(
        DateTimeFormatter.ISO_LOCAL_TIME));
    startTime.setPrefWidth(60);
    Label to = new Label("to: ");
    TextField endTime = new TextField(LocalTime.now().format(
        DateTimeFormatter.ISO_LOCAL_TIME));
    endTime.setPrefWidth(60);
    Button create = new Button("Create");
    create.setPrefWidth(60);
    create.setTextFill(Paint.valueOf("White"));
    create.setStyle("-fx-background-color:  #348e2f");
    create.setOnAction(event -> {
      try
      {
        createRectangleWithText();
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    });
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
    TableColumn<User, ComboBox> attends = new TableColumn<>("Attends");
    attendance.getColumns().addAll(firstName, lastName, attends);
    attendance.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    attendance.setPrefWidth(600);
    attendance.setPrefHeight(150);
    attendance.setPadding(Insets.EMPTY);
    employeeAttendance.getChildren().add(attendance);


    dateTime.setPadding(new Insets(20, 0, 0, 20));
    descr.setPadding(new Insets(20, 0, 10, 20));

    insert.addAll(topBar, dateTime, descr, employeeAttendance);



    Scene scene = new Scene(parent);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

  }

  public void createMeetingObject(){
    try
    {
      tilePane.getChildren().add(createRectangleWithText());
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

  public StackPane createRectangleWithText() throws SQLException
  {

    Rectangle rect = new Rectangle(200, 200);
    rect.setFill(Color.rgb(84,73,151));
    rect.setArcWidth(20);
    rect.setArcHeight(20);

//    "Tryout","This is a test", Date.valueOf(LocalDate.of(2023,4,29)), Time.valueOf(
//        LocalTime.of(12,0,0)),Time.valueOf(LocalTime.of(13,0,0))
    String title="Tryout";
    Text titleText = new Text(title);
    double textWidth = titleText.getBoundsInLocal().getWidth();
    double textHeight = titleText.getBoundsInLocal().getHeight();
    titleText.setLayoutX((200 - textWidth) / 2);
    titleText.setLayoutY((200 - textHeight) / 2);
    titleText.setFont(Font.font(30));
    titleText.setFill(Color.WHITE);

    String description="This is a test";
    Text descriptionText=new Text(description);
    textWidth = descriptionText.getBoundsInLocal().getWidth();
    textHeight = descriptionText.getBoundsInLocal().getHeight();
    descriptionText.setLayoutX((200 - textWidth) / 2);
    descriptionText.setLayoutY((200 - textHeight) / 2);
    descriptionText.setFont(Font.font(20));
    descriptionText.setFill(Color.WHITE);

    Date date=new java.sql.Date(2023,5,6);
    Time startTime= Time.valueOf(LocalTime.of(12,0,0));
    Time endTime=Time.valueOf(LocalTime.of(13,0,0));

    Text dateText= new Text(date.toString());
    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
    Text startText= new Text(formatter.format(startTime));
    Text endText= new Text(formatter.format(endTime));
    dateText.setFill(Color.WHITE);
    startText.setFill(Color.WHITE);
    endText.setFill(Color.WHITE);

    meetingViewModel.addMeeting(title,description,date,startTime,endTime);

    VBox vBox=new VBox(titleText,descriptionText,dateText,startText,endText);
    return new StackPane(rect,vBox);
  }

}

