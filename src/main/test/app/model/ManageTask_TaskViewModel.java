package app.model;

import app.shared.Task;
import app.viewmodel.TasksViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;

public class ManageTask_TaskViewModel
{
  private TasksViewModel viewModel;
  private Model model;
  private Task task;

  @BeforeEach void setUp(){
    this.model = Mockito.mock(ModelManager.class);
    this.viewModel = new TasksViewModel(model);

    this.task = new Task("Dummy","DummyDescription", Date.valueOf("2023-05-12"),"",1);
  }

  @Test void create_and_edit_task() throws Exception{
    viewModel.addTask(task.title(), task.description(), task.date(),task.status(),
        task.business_id());
    Mockito.verify(model,Mockito.times(1)).addTask(task.title(), task.description(), task.date(),task.status(),
        task.business_id());

    viewModel.editTask(task,task);
    Mockito.verify(model,Mockito.times(1)).editTask(task,task);
  }

  @Test void remove_a_task_(){
    //Now the methods don't exist
    //viewModel.removeTask(task);
    //Mockito.verify(model, Mockito.times(1)).removeTask(task);
  }

  @Test void getTask() throws Exception{
    viewModel.getTasks();
    Mockito.verify(model, Mockito.times(2)).getTasks();
  }


}
