package app.shared;

import app.model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable
{ private String title;
  private int priorityPoints; //The idea for this is that it can be set from 0-5, and the default value would be 0 (low priority).
  private String description;
  private ArrayList<User> assignedEmployees; //This would be for the assigned employees to complete the task. It could also be left empty.
  private String startDate; // Leave it as a string for now until we see what type of object we will use for the dates.
  private String endDate;  // same as startDate.

  public Task(String title, String description, String startDate, String endDate)
  {
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    assignedEmployees = new ArrayList<User>();
    priorityPoints = 0;
  }

  public void assignEmployee(User e)
  {
    assignedEmployees.add(e);
  }

  public void removeEmployee(User e)
  {
    assignedEmployees.remove(e);
  }

  public void setPriorityPoints(int points)
  {
    priorityPoints = points;
  }

  public void setStartDate(String startDate)
  {
    this.startDate = startDate;
  }

  public void setEndDate(String endDate)
  {
    this.endDate = endDate;
  }

  public void setDescription(String description)
  {
    this.description = description;
 }

  public void setTitle(String title)
  {
    this.title = title;
 }

  public String getTitle()
  {
    return title;
  }

  public ArrayList<User> getAssignedEmployees()
  {
    return assignedEmployees;
  }

  public User[] getAssignedEmployeesArray()
  {
    User[] employees = new User[assignedEmployees.size()];
    employees = assignedEmployees.toArray(employees);

    return employees;
  }


  public int getPriorityPoints()
  {
    return priorityPoints;
  }

  public String getDescription()
  {
    return description;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public String getStartDate()
  {
    return startDate;
  }
}
