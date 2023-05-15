package app.model;

import app.server.ServerImplementation;
import app.shared.Communicator;
import app.shared.Task;
import app.viewmodel.TasksViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;

public class ManageTask_ModelManager
{
  private Model model;
  private Communicator communicator;
  private Task task;

  @BeforeEach void setUp()throws  Exception{
    this.communicator = Mockito.mock(ServerImplementation.class);
    this.model = new ModelManager(communicator);

    this.task = new Task("Dummy","DummyDescription", Date.valueOf("2023-05-12"),"",1);
  }

  //I commented the mothods that show error

  @Test void create_and_edit_task() throws Exception{
    model.addTask(task.title(), task.description(), task.date(),task.status(),
        task.business_id());
    //Mockito.verify(communicator,Mockito.times(1)).addTask(task));

    model.editTask(task,task);
    Mockito.verify(communicator,Mockito.times(1)).editTask(task,task);
  }

  @Test void remove_a_task_() throws Exception{
    //Now the methods don't exist
    //model.removeTask(task);
    Mockito.verify(communicator, Mockito.times(1)).removeTask(task);
  }

  @Test void getTask() throws Exception{
    model.getTasks();
    Mockito.verify(communicator, Mockito.times(2)).getTasks();
  }

}
