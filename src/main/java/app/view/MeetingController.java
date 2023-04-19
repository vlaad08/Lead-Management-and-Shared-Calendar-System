package app.view;

import app.viewmodel.MeetingViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class MeetingController
{
  @FXML private Button Calendar;

  private Region root;
  private ViewHandler viewHandler;
  private MeetingViewModel meetingViewModel;

  public void init(ViewHandler viewHandler, MeetingViewModel meetingViewModel, Region root){
    this.viewHandler = viewHandler;
    this.meetingViewModel = meetingViewModel;
    this.root = root;

    //bs comes below
  }

  public Region getRoot()
  {
    return root;
  }

  public void  reset(){} //why not

  @FXML public void onCalendarView(){
    viewHandler.openView("Calendar");
  }

}
