package app.view;


import app.viewmodel.MeetingViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.sql.Date;

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

  }

  public void removeMeeting(){
    //meetingViewModel.removeMeeting(meeting);
  }

  public void editMeeting(){

  }

  public void createRectangleWithText() throws SQLException
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
    StackPane stackPane=new StackPane(rect,vBox);
    tilePane.getChildren().add(stackPane);
  }

}

