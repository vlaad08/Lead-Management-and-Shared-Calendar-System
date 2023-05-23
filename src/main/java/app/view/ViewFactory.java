package app.view;

import app.viewmodel.ViewModelFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

import java.io.IOError;
import java.io.IOException;
import java.sql.SQLException;

public class ViewFactory
{
  private final ViewHandler viewHandler;
  private final ViewModelFactory viewModelFactory;
  private MeetingController meetingController;
  private CalendarController calendarController;
  private TaskController taskController;
  private AvailableClientsController availableClientsController;
  private AllUsersController allUsersController;
  private ManageLeadsController manageLeadsController;
  private LoginController loginController;

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
          case "Login" -> loadLoginView();
          case "Calendar" -> loadCalendarView();
          case "Meeting" -> loadMeetingView();
          case "Task" -> loadTasksView();
          case "AvailableClients" -> loadAvailableClientsView();
          case "AllUsers" -> loadAllUsersView();
          case "Leads" -> loadLeadsView();
          default -> throw new IllegalArgumentException("Unknow view id: " + id);
        };
  }

  public Region loadCalendarView()
  {
    if (calendarController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Calendar.fxml"));
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
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
    }

    return meetingController.getRoot();
  }

  public Region loadTasksView()
  {
    if (taskController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Task.fxml"));
      try
      {
        Region root = loader.load();
        taskController = loader.getController();
        taskController.init(viewHandler, viewModelFactory.getTasksViewModel(),
            root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }
    return taskController.getRoot();
  }

  public Region loadAvailableClientsView()
  {
    if (availableClientsController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/AvailableClients.fxml"));
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

  public Region loadAllUsersView()
  {
    if (allUsersController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/AllUsers.fxml"));
      try
      {
        Region root = loader.load();
        allUsersController = loader.getController();
        allUsersController.init(viewHandler, viewModelFactory.getAllClientsViewModel(), root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }
    return allUsersController.getRoot();
  }

  public Region loadLeadsView()
  {
    if (manageLeadsController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/ManageLeads.fxml"));
      try
      {
        Region root = loader.load();
        manageLeadsController = loader.getController();
        manageLeadsController.init(viewHandler, viewModelFactory.getLeadsViewModel(),
            root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }

    return manageLeadsController.getRoot();
  }

  public Region loadLoginView()
  {
    if(loginController == null)
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/app/Login.fxml"));
      try
      {
        Region root = loader.load();
        loginController = loader.getController();
        loginController.init(viewHandler, viewModelFactory.getSelectRoleViewModel(),
            root);
      }
      catch (IOException e)
      {
        throw new IOError(e);
      }
    }

    return loginController.getRoot();
  }
}
