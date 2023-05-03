package app.model;

import app.JDBC.SQLConnection;
import app.shared.Meeting;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class ModelManager implements Model
{
  private SQLConnection connection;
  public ModelManager(){
    try{
      connection = SQLConnection.getInstance();
    }catch (SQLException e){
      e.printStackTrace();
    }
  }

  @Override public void addMeeting(Date startDate, Date endDate, String description, ArrayList<User> employees)
  {
    
  }

  @Override public void removeMeeting(Meeting meeting)
  {

  }

  @Override public ArrayList<Meeting> getMeetings()
  {
    return null;
  }

  @Override public void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description,
      ArrayList<User> employees)
  {

  }
}
