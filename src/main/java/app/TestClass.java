package app;

import app.JDBC.SQLConnection;
import app.model.ConstraintChecker;
import app.server.Server;
import app.server.ServerImplementation;
import app.model.MessageListener;
import app.model.ModelManager;
import app.shared.Communicator;
import app.viewmodel.MeetingViewModel;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

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


      /*
      try{
        Time start = Time.valueOf(LocalTime.parse("12321"));
        if(start instanceof Time){
          System.out.println("Test pass");;
        }
      }catch (Exception e){
        System.out.println("Exeption of input");;
      }

       */

      boolean b = ConstraintChecker.checkDateAndTime(LocalDate.now(),"12:45","15:50");
    System.out.println("Expect true: "+b);
    boolean t2 = ConstraintChecker.checkDateAndTime(LocalDate.of(2022,6,8),"12:45","15:50");
    System.out.println("Expect false: "+t2);
    boolean t3 = ConstraintChecker.checkDateAndTime(LocalDate.now(),"06:40","15:50");
    System.out.println("Expect false: "+t3);
    boolean t4 = ConstraintChecker.checkDateAndTime(LocalDate.of(2023,06,12),"06:40","15:50");
    System.out.println("Expect true: "+t4);
    boolean t5 = ConstraintChecker.checkDateAndTime(LocalDate.of(2023,06,12),"16:40","10:50");
    System.out.println("Expect false: "+t5);
    boolean t6 = ConstraintChecker.checkDateAndTime(LocalDate.of(2023,06,12),"16:4562","10:50");
    System.out.println("Expect false - illigal input: "+t6);


  }
}
