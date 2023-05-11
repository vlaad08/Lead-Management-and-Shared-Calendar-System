package app.viewmodel;

import app.model.Model;
import app.shared.Task;
import java.sql.Date;

import java.util.ArrayList;

public class TasksViewModel
{
  private Model model;

  public TasksViewModel(Model model){
    this.model = model;
  }

  public void addTask(String title, String description, Date date, String status, int business_id){
//    model.addTask(title, description, date, status, business_id);
  }

  public ArrayList<Task> getTasks(){
//    return model.getTasks();
    return null;
  }
}
