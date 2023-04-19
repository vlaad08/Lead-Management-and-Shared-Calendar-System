package app.view;

import app.viewmodel.ViewModelFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewHandler
{
  private Stage primaryStage;
  private Scene currentScene;
  private ViewFactory viewFactory;

  public ViewHandler(ViewModelFactory viewModelFactory){
    viewFactory = new ViewFactory(this,viewModelFactory);
    currentScene = new Scene(new Region());
  }

  public void start(Stage primaryStage){
    this.primaryStage = primaryStage;
    openView("Calendar");
  }

  public void openView(String id){
    Region viewFactoryRegion = viewFactory.loadView(id);
    currentScene.setRoot(viewFactoryRegion);
    primaryStage.setScene(currentScene);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Meeting");
    primaryStage.show();
  }

  public void close(){
    primaryStage.close();
  }

}
