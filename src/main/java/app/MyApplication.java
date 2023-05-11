package app;

import app.model.MessageListener;
import app.model.Model;
import app.model.ModelManager;
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
    Registry registry = LocateRegistry.getRegistry(7430);
    Communicator communicator = (Communicator) registry.lookup("communicator");

    Model model = new ModelManager(communicator);


    MessageListener messageListener = new MessageListener(model);
    //TaskListener
    //LeadListener


    communicator.addMeetingListener(messageListener);



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
