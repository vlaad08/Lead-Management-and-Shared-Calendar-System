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
    Registry registry = LocateRegistry.getRegistry(5168);
    Communicator communicator = (Communicator) registry.lookup("communicator");

    ModelManager modelManager = new ModelManager(communicator);
    Model model = modelManager;
    ReloadData data = modelManager ;


    Listener listener = new Listener(data);
    //All the liseners are in the Listener class
    //TaskListener taskListener = new TaskListener(data);
    //LeadListener

    communicator.addListener(listener);
    //communicator.addTaskListener(taskListener);



    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("Calendar.fxml"));
    Scene scene= new Scene(loader.load());
    primaryStage.setResizable(false);


    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setScene(scene);
    primaryStage.show();

    //For creating connection with the server

    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);



  }
}
