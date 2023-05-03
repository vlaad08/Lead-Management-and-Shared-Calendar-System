package app.model;

import app.shared.Meeting;

import java.util.ArrayList;
import java.util.Date;

public interface Model
{
  void addMeeting(Date startDate, Date endDate, String description, ArrayList<User> employees);
  void removeMeeting(Meeting meeting);
  ArrayList<Meeting> getMeetings();
  void editMeeting(Date oldStartDate, Date oldEndDate,
      Date startDate, Date endDate, String description, ArrayList<User> employees);
}
