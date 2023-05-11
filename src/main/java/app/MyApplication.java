package app;

import app.model.ClientListener;
import app.model.Model;
import app.model.ModelManager;
import app.server.Server;
import app.view.ViewHandler;
import app.viewmodel.ViewModelFactory;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
    primaryStage.setTitle("SelectRole");
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("SelectRole.fxml"));
    Scene scene= new Scene(loader.load());
//    primaryStage.setResizable(false);
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setScene(scene);
    primaryStage.show();

    //For creating connection with the server
    Registry registry = LocateRegistry.getRegistry(1099);
    Server server = (Server) registry.lookup("communicator");
    ClientListener listener = new ClientListener(server);

    Model model = new ModelManager(listener);
    ViewModelFactory viewModelFactory = new ViewModelFactory(model);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);
  }
}
