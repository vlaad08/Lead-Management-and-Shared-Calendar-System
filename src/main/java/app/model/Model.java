package app.model;

import app.shared.Meeting;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;

public interface Model
{
  void addMeeting(String title, String description,Date date, Time startTime, Time endTime)
      throws SQLException;
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();
  void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description, ArrayList<User> employees);
}
