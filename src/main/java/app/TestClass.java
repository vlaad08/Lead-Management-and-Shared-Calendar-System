package app;

import app.JDBC.SQLConnection;
import app.server.Server;
import app.server.ServerImplementation;
import app.model.MessageListener;
import app.model.ModelManager;
import app.shared.Communicator;
import app.viewmodel.MeetingViewModel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestClass
{
  public static void main(String[] args) throws Exception
  {

//<<<<<<< Updated upstream
//
//    SQLConnection sqlConnection = SQLConnection.getInstance();
//    /*
//    sqlConnection.createMeeting("Meeting2","test",
//        Date.valueOf(LocalDate.of(2023,6,5)),
//        Time.valueOf(LocalTime.of(10,30,0)),
//        Time.valueOf(LocalTime.of(10,45,0)), "example@gmail.com");
//
//     */
//    Registry registry = LocateRegistry.getRegistry(1099);
//    Server server = (Server) registry.lookup("communicator");
//    Communicator listener = new ClientListener(server);
//
//    System.out.println(listener.getMeetings());
//
//
//=======
//
//    SQLConnection sqlConnection = SQLConnection.getInstance();
//    /*
//    sqlConnection.createMeeting("Meeting2","test",
//        Date.valueOf(LocalDate.of(2023,6,5)),
//        Time.valueOf(LocalTime.of(10,30,0)),
//        Time.valueOf(LocalTime.of(10,45,0)), "example@gmail.com");
//
//     */
//
//    Registry registry = LocateRegistry.getRegistry(1024);
//    Server server = (Server) registry.lookup("communicator");
//    MessageListener listener = new MessageListener(server);
//
//
//    System.out.println(new MeetingViewModel(new ModelManager(listener)).getMeetings());

  }
}
