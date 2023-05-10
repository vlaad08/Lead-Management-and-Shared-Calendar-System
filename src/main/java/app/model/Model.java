package app.model;

import app.shared.Meeting;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public interface Model
{
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email);
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();
  void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description, ArrayList<User> employees);
}
