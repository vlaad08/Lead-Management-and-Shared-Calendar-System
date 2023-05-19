package app.view;


import app.shared.Meeting;
import app.viewmodel.MeetingViewModel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
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


  private final ObjectProperty<ObservableList<Meeting>> meetings = new SimpleObjectProperty<>();

  public void init(ViewHandler viewHandler, MeetingViewModel meetingViewModel, Region root)
      throws SQLException
  {

    this.viewHandler = viewHandler;
    this.meetingViewModel = meetingViewModel;
    this.root = root;

    meetingViewModel.addPropertyChangeListener(this);



    //bs comes below

    Draw.hoverButtonNavbar(plansButton,taskButton,leadButton,availableButton,clientsButton,manageLeadsButton,closeButton);


    tilePane.setHgap(40);
    tilePane.setVgap(50);
    tilePane.setPrefTileWidth(300);
    tilePane.setPrefColumns(3);
    tilePane.setPrefRows(1);
    tilePane.setTileAlignment(Pos.CENTER_LEFT);


    meetingViewModel.bindMeetings(meetings);


    Draw.drawMeetings(tilePane, meetings.get(), meetingViewModel);
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
        case "All Users" -> viewHandler.openView("AllUsers");
        case "Manage leads" -> viewHandler.openView("Leads");
      }
    }
  }


  public void addMeeting() throws SQLException, RemoteException
  {
    Draw.drawMeetingPopUp(meetingViewModel);
  }




  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadMeetings"))
    {
        Platform.runLater(()->{
            Draw.drawMeetings(tilePane, meetings.get(), meetingViewModel);
        });

    }
  }
}

