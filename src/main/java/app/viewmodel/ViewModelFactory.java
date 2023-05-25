package app.viewmodel;

import app.model.*;

public class ViewModelFactory
{
  private final CalendarViewModel calendarViewModel;
  private final MeetingViewModel meetingViewModel;
  private final TasksViewModel tasksViewModel;
  private final AvailableClientsViewModel availableClientsViewModel;
  private final AllUsersViewModel allUsersViewModel;
  private final LeadsViewModel leadsViewModel;
  private final LoginViewModel loginViewModel;

  public ViewModelFactory(ModelCalendar modelCalendar, ModelLead modelLead, ModelLogin modelLogin, ModelMeetingAndTask modelMeetingAndTask, ModelUser modelUser){
    this.calendarViewModel = new CalendarViewModel(modelCalendar);
    this.meetingViewModel = new MeetingViewModel(modelMeetingAndTask);
    this.tasksViewModel = new TasksViewModel(modelMeetingAndTask);
    this.availableClientsViewModel = new AvailableClientsViewModel(modelCalendar);
    this.allUsersViewModel = new AllUsersViewModel(modelUser);
    this.leadsViewModel = new LeadsViewModel(modelLead);
    this.loginViewModel =new LoginViewModel(modelLogin);
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
