package app.viewmodel;

import app.model.Model;

public class ViewModelFactory
{
  private CalendarViewModel calendarViewModel;
  private MeetingViewModel meetingViewModel;
  private TasksViewModel tasksViewModel;
  private AvailableClientsViewModel availableClientsViewModel;
  private AllClientsViewModel allClientsViewModel;
  private LeadsViewModel leadsViewModel;

  public ViewModelFactory(Model model){
    this.calendarViewModel = new CalendarViewModel(model);
    this.meetingViewModel = new MeetingViewModel(model);
    this.tasksViewModel = new TasksViewModel(model);
    this.availableClientsViewModel = new AvailableClientsViewModel(model);
    this.allClientsViewModel = new AllClientsViewModel(model);
    this.leadsViewModel = new LeadsViewModel(model);
  }

  public CalendarViewModel getCalendarViewModel(){
    return calendarViewModel;
  }

  public MeetingViewModel getMeetingViewModel(){
    return meetingViewModel;
  }

  public TasksViewModel getTasksViewModel(){
    return tasksViewModel;
  }

  public AvailableClientsViewModel getAvailableClientsViewModel(){
    return availableClientsViewModel;
  }

  public AllClientsViewModel getAllClientsViewModel(){
    return allClientsViewModel;
  }

  public LeadsViewModel getLeadsViewModel(){
    return leadsViewModel;
  }
}
