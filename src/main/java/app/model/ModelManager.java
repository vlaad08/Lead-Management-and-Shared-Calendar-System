package app.model;

import app.JDBC.SQLConnection;
import app.shared.Meeting;
import app.shared.Task;

import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class ModelManager implements Model
{
  private User user;
  private ClientListener clientListener;

  public ModelManager(ClientListener clientListener){
    this.clientListener = clientListener;
    user=new User("employee-1", "password", "wasdwasd@1234.com","Craig","Larhman", false);
  }

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email) {
    if(endTime.before(startTime)){
      throw new IllegalArgumentException("startTime must be before endTime");
    }else {
      try{
        clientListener.addMeeting(new Meeting(title, description, date, startTime, endTime, email));
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  @Override public ArrayList<Meeting> getMeetings() {
    try{
      return clientListener.getMeetings();
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void addTask(String title, String description, Date date, String status, int business_id) {
    try{
      clientListener.addTask(new Task(title, description, date, status, business_id));
    }catch (RemoteException e){
      e.printStackTrace();
    }
  }

  @Override public ArrayList<Task> getTasks()
  {
    try{
      return clientListener.getTasks();
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  public void setUser()
  {
    user.setManager(true);
  }

  @Override public void removeMeeting(Meeting meeting)
  {
  }



  public boolean checkUser(){
    if (user.isManager()==false)
    {
      return false;
    }
    return true;
  }
}
