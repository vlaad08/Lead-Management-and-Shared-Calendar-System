package app.view;

import app.viewmodel.ViewModelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import java.io.IOError;
import java.io.IOException;

public class ViewFactory
{
  private ViewHandler viewHandler;
  private ViewModelFactory viewModelFactory;
  private MeetingController meetingController;
  private CalendarController calendarController;

  public ViewFactory (ViewHandler viewHandler, ViewModelFactory viewModelFactory){
    this.viewModelFactory = viewModelFactory;
    this.viewHandler = viewHandler;
    meetingController = null;
    calendarController = null;
  }

  public Region loadView(String id){
    return switch(id){
      case"Calendar" -> loadCalendarView();   //other views can be added
      case"Meeting" -> loadMeetingView();
      default -> throw new IllegalArgumentException("Unknow view id: " +id);
    };
  }

  public Region loadCalendarView(){
    if(calendarController == null){
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/calendar.fxml"));
      try{
        Region root = loader.load();
        calendarController = loader.getController();
        //calendarController.initialize();//WHAT THE FUCK IS THE URL SHIT IN THERE???
        calendarController.init(viewHandler, viewModelFactory.getCalendarViewModel(), root); //<-- that method exists idunno what the problem is
      } catch(IOException e){
        throw new IOError(e);
      }
    }

    //calendarController.reset();
    return calendarController.getRoot();
  }

  public Region loadMeetingView(){
    if(meetingController == null){
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Meeting.fxml"));
      try{
        Region root = loader.load();
        meetingController = loader.getController();
        meetingController.init(viewHandler, viewModelFactory.getMeetingViewModel(), root);
      } catch (IOException e){
        throw new IOError(e);
      }
    }

    return meetingController.getRoot();
  }



}
