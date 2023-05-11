package app.model;

import app.JDBC.SQLConnection;
import app.shared.Meeting;

import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelManager implements Model
{
  private final User user=new User("employee-1","password","faafas@123123.com","Craig","Lahrman",false);
  private ClientListener clientListener;
  PropertyChangeSupport support;
  public ModelManager(ClientListener clientListener){
    this.clientListener = clientListener;
    support=new PropertyChangeSupport(this);
  }

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email) {
   try{
     clientListener.addMeeting(new Meeting(title, description, date, startTime, endTime, email));
   }catch (Exception e){
     e.printStackTrace();
   }
  }

  @Override public void removeMeeting(Meeting meeting)
  {

  }

  @Override public ArrayList<Meeting> getMeetings() {
    try{
      return clientListener.getMeetings();
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  @Override public void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description,
      ArrayList<User> employees)
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
