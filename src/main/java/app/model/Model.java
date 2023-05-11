package app.model;

import app.shared.Meeting;
import app.shared.Task;

import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public interface Model
{
  //For first Use case
  void addMeeting(String title, String description, java.sql.Date date, Time startTime, Time endTime, String email);
  ArrayList<Meeting> getMeetings();
  //For second use case
  void addTask(String title, String description, Date date, String status, int business_id);
  ArrayList<Task> getTasks();
  void removeMeeting(Meeting meeting);


  boolean checkUser();
  void setUser();
}
