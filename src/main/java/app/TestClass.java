package app;

import app.JDBC.SQLConnection;
import app.model.ClientListener;
import app.model.ModelManager;
import app.server.Server;
import app.server.ServerImplementation;
import app.viewmodel.MeetingViewModel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestClass
{
  public static void main(String[] args) throws Exception
  {


    SQLConnection sqlConnection = SQLConnection.getInstance();
    /*
    sqlConnection.createMeeting("Meeting2","test",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(10,30,0)),
        Time.valueOf(LocalTime.of(10,45,0)), "example@gmail.com");

     */

    Registry registry = LocateRegistry.getRegistry(1024);
    Server server = (Server) registry.lookup("communicator");
    ClientListener listener = new ClientListener(server);


    System.out.println(new MeetingViewModel(new ModelManager(listener)).getMeetings());

  }
}
