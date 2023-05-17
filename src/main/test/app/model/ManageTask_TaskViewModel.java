package app.model;

import app.shared.Task;
import app.viewmodel.TasksViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageTask_TaskViewModel
{
  private TasksViewModel viewModel;
  private Model model;
  private Task task;

  @BeforeEach void setUp(){
    this.model = Mockito.mock(ModelManager.class);
    this.viewModel = Mockito.mock(TasksViewModel.class);

    this.task = new Task("Dummy","DummyDescription", Date.valueOf("2023-05-12"),"",1);
  }

  @Test void create_and_edit_task() throws Exception{
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("agostonbabicz@gmail.com");
    emails.add("emanuelduca@gmail.com");
    viewModel.addTask(task.getTitle(), task.getDescription(), task.getDate(),task.getStatus(),
        task.getBusiness_id(),emails);
    Mockito.verify(viewModel,Mockito.times(1)).addTask(task.getTitle(), task.getDescription(), task.getDate(),task.getStatus(),
        task.getBusiness_id(),emails);

    viewModel.editTask(task,task,emails);
    Mockito.verify(viewModel,Mockito.times(1)).editTask(task,task,emails);
  }

  @Test void remove_a_task_() throws SQLException, RemoteException
  {
    //Now the methods don't exist
    viewModel.removeTask(task);
    Mockito.verify(viewModel, Mockito.times(1)).removeTask(task);
  }

  @Test void getTask() throws Exception{
    viewModel.getTasks();
    Mockito.verify(viewModel, Mockito.times(1)).getTasks();
  }


}
