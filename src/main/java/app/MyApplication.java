package app;

import app.model.*;
import app.shared.Communicator;
import app.view.ViewHandler;
import app.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MyApplication extends Application
{
  public static void main(String[] args)
  {
    launch();
  }

  @Override public void start(Stage primaryStage) throws Exception
  {
    Registry registry = LocateRegistry.getRegistry(6123);
    Communicator communicator = (Communicator) registry.lookup("communicator");

    ModelManager model = new ModelManager(communicator);
    ModelCalendar modelCalendar = model;
    ModelLead modelLead = model;
    ModelListener modelListener = model;
    ModelLogin modelLogin = model;
    ModelMeetingAndTask modelMeetingAndTask = model;
    ModelUser modelUser = model;

    Listener listener = new Listener(modelListener);

    communicator.addListener(listener);
    primaryStage.setResizable(false);
    primaryStage.initStyle(StageStyle.UNDECORATED);


    ViewModelFactory viewModelFactory = new ViewModelFactory(modelCalendar,modelLead,modelLogin,modelMeetingAndTask,modelUser);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);



  }
}
