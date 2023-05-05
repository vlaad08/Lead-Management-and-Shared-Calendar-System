package app.model;

import app.JDBC.SQLConnection;
import app.shared.Meeting;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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

  @Override public void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime) throws SQLException
  {
    connection.createMeeting(title,description,date,startTime,endTime);
  }

  @Override public void editMeeting(java.sql.Date oldStartDate,
      java.sql.Date oldEndDate, java.sql.Date startDate, java.sql.Date endDate,
      String description, ArrayList<User> employees)
  {

  }

  @Override public void removeMeeting(Meeting meeting)
  {

  }

  @Override public ArrayList<Meeting> getMeetings()
  {
    return null;
  }

}
