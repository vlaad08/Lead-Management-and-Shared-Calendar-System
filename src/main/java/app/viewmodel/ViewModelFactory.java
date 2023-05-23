package app.viewmodel;

import app.model.Model;

public class ViewModelFactory
{
  private final CalendarViewModel calendarViewModel;
  private final MeetingViewModel meetingViewModel;
  private final TasksViewModel tasksViewModel;
  private final AvailableClientsViewModel availableClientsViewModel;
  private final AllUsersViewModel allUsersViewModel;
  private final LeadsViewModel leadsViewModel;
  private final LoginViewModel loginViewModel;

  public ViewModelFactory(Model model){
    this.calendarViewModel = new CalendarViewModel(model);
    this.meetingViewModel = new MeetingViewModel(model);
    this.tasksViewModel = new TasksViewModel(model);
    this.availableClientsViewModel = new AvailableClientsViewModel(model);
    this.allUsersViewModel = new AllUsersViewModel(model);
    this.leadsViewModel = new LeadsViewModel(model);
    this.loginViewModel =new LoginViewModel(model);
  }

  public LoginViewModel getSelectRoleViewModel()
  {
    return loginViewModel;
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

  public AllUsersViewModel getAllClientsViewModel(){
    return allUsersViewModel;
  }

  public LeadsViewModel getLeadsViewModel(){
    return leadsViewModel;
  }
}
