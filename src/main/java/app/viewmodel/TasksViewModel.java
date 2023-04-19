package app.viewmodel;

import app.model.Model;
import app.view.ViewHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class TasksViewModel
{
  private Model model;

  public TasksViewModel(Model model){
    this.model = model;
  }
}
