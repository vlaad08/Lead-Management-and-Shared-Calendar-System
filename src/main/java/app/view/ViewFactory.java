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
  private TasksController tasksController;
  private AvailableClientsController availableClientsController;
  private AllClientsController allClientsController;
  private LeadsController leadsController;

  public ViewFactory(ViewHandler viewHandler, ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    this.viewHandler = viewHandler;
    meetingController = null;
    calendarController = null;
  }

  public Region loadView(String id)
  {
    return switch (id)
        {
          case "Calendar" -> loadCalendarView();   //other views can be added
          case "Meeting" -> loadMeetingView();
          case "Tasks" -> loadTasksView();
          case "AvailableClients" -> loadAvailableClientsView();
          case "AllClients" -> loadAllClientsView();
          case "Leads" -> loadLeadsView();
          default -> throw new IllegalArgumentException("Unknow view id: " + id);
        };
  }

  public Region loadCalendarView()
  {
    if (calendarController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/calendar.fxml"));
      try
      {
        Region root = loader.load();
        calendarController = loader.getController();
        //calendarController.initialize();//WHAT THE FUCK IS THE URL SHIT IN THERE???
        calendarController.init(viewHandler, viewModelFactory.getCalendarViewModel(), root); //<-- that method exists idunno what the problem is
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }

    //calendarController.reset();
    return calendarController.getRoot();
  }

  public Region loadMeetingView()
  {
    if (meetingController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Meeting.fxml"));
      try
      {
        Region root = loader.load();
        meetingController = loader.getController();
        meetingController.init(viewHandler, viewModelFactory.getMeetingViewModel(), root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }

    return meetingController.getRoot();
  }

  public Region loadTasksView()
  {
    if (tasksController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Task.fxml"));
      try
      {
        Region root = loader.load();
        tasksController = loader.getController();
        tasksController.init(viewHandler, viewModelFactory.getTasksViewModel(),
            root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }
    return tasksController.getRoot();
  }

  public Region loadAvailableClientsView()
  {
    if (availableClientsController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Available.fxml"));
      try
      {
        Region root = loader.load();
        availableClientsController = loader.getController();
        availableClientsController.init(viewHandler, viewModelFactory.getAvailableClientsViewModel(), root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }
    return availableClientsController.getRoot();
  }

  public Region loadAllClientsView()
  {
    if (allClientsController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Clients.fxml"));
      try
      {
        Region root = loader.load();
        allClientsController = loader.getController();
        allClientsController.init(viewHandler, viewModelFactory.getAllClientsViewModel(), root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }
    return allClientsController.getRoot();
  }

  public Region loadLeadsView()
  {
    if (leadsController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Leads.fxml"));
      try
      {
        Region root = loader.load();
        leadsController = loader.getController();
        leadsController.init(viewHandler, viewModelFactory.getLeadsViewModel(),
            root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }

    return leadsController.getRoot();
  }
}
