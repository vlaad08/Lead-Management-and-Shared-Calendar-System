package app.shared;
import app.model.Model;
import app.model.ModelManager;
import app.viewmodel.TasksViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class TestTasksViewModel
{
  private Model model;
  private Communicator communicator;
  private TasksViewModel tasksViewModel;
  private ObjectProperty<ObservableList<Task>> tasks;
  private Task dummy;

  @BeforeEach
  void setUp() throws SQLException, RemoteException
  {
    communicator = Mockito.mock(Communicator.class);
    model=Mockito.mock(ModelManager.class);
    tasksViewModel = Mockito.mock(TasksViewModel.class);
    tasks=new SimpleObjectProperty<>();
    tasksViewModel.bindTask(tasks);
    dummy=new Task("Dummy","DummyDescription", Date.valueOf("2023-05-12"),"",1);
  }

  @Test
  void  new_task_is_created_and_stored() throws SQLException, RemoteException
  {
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("example@gmail.com");
    tasksViewModel.addTask(dummy.getTitle(), dummy.getDescription(), dummy.getDate(),
        dummy.getStatus(), dummy.getBusiness_id(),emails);
    Mockito.verify(tasksViewModel,Mockito.times(1)).addTask(dummy.getTitle(), dummy.getDescription(), dummy.getDate(),
        dummy.getStatus(), dummy.getBusiness_id(),emails);
  }

  @Test
  void new_task_is_not_stored_without_title()
      throws SQLException, RemoteException
  {
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("example@gmail.com");
    Task temp=new Task("","DummyDescription", Date.valueOf("2023-05-12"),"",1);
    tasksViewModel.addTask(dummy.getTitle(), dummy.getDescription(), dummy.getDate(),
        dummy.getStatus(), dummy.getBusiness_id(),emails);
    Mockito.verify(communicator,Mockito.never()).createTask(temp);
  }

  

}
