package app.model;

import app.shared.Meeting;

import java.util.ArrayList;
import java.util.Date;

public class ModelManager implements Model
{
  @Override public void addMeeting(Date startDate, Date endDate,
      String description, ArrayList<Employee> employees)
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
      ArrayList<Employee> employees)
  {

  }
}
