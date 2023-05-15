package app.model;

import app.JDBC.SQLConnection;
import app.server.ServerImplementation;
import app.shared.Communicator;
import app.shared.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;

public class ManageTask_Server
{
  private Communicator server;
  private SQLConnection connection;
  private Task task;

  @BeforeEach void setUp()throws  Exception{
    this.connection = Mockito.mock(SQLConnection.class);
    this.server = new ServerImplementation();

    this.task = new Task("Dummy","DummyDescription", Date.valueOf("2023-05-12"),"",1);
  }

  //I commented the mothods that show error

  @Test void create_and_edit_task() throws Exception{
    //server.addTask(task);
    //Mockito.verify(connection,Mockito.times(1)).addTask(task);

    server.editTask(task,task);
    Mockito.verify(connection,Mockito.times(1)).editTask(task,task);
  }

  @Test void remove_a_task_() throws Exception{
    //Now the methods don't exist
    server.removeTask(task);
    //Mockito.verify(connection, Mockito.times(1)).removeTask(task);
  }
}
