package app.viewmodel;

import app.model.Model;

public class ViewModelFactory
{
  private CalendarViewModel calendarViewModel;
  private MeetingViewModel meetingViewModel;

  public ViewModelFactory(Model model){
    this.calendarViewModel = new CalendarViewModel(model);
    this.meetingViewModel = new MeetingViewModel(model);
  }

  public CalendarViewModel getCalendarViewModel(){
    return calendarViewModel;
  }

  public MeetingViewModel getMeetingViewModel(){
    return meetingViewModel;
  }

}
