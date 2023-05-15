package app.view;


import app.shared.Meeting;
import app.viewmodel.MeetingViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

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


    Draw.drawMeetings(tilePane, meetings, meetingViewModel);
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
    Draw.drawMeetingPopUp(tilePane, meetingViewModel);
  }




  public void removeMeeting(){
    //meetingViewModel.removeMeeting(meeting);
  }

  public void editMeeting(){

  }



  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings"))
    {
        Platform.runLater(()->{



            Draw.drawMeetings(tilePane, meetings, meetingViewModel);

        });

    }
  }
}

