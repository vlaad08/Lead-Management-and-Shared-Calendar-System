package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MyApplication extends Application
{
  public static void main(String[] args)
  {
    launch();
  }

  @Override public void start(Stage primaryStage) throws Exception
  {
    primaryStage.setTitle("Calendar");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("calendar.fxml"));
    Scene scene= new Scene(loader.load());
    primaryStage.setResizable(false);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
