package app.shared;

import app.model.Employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Task implements Serializable
{ private String title;
  private int priorityPoints; //The idea for this is that it can be set from 0-5, and the default value would be 0 (low priority).
  private String description;
  private ArrayList<Employee> assignedEmployees; //This would be for the assigned employees to complete the task. It could also be left empty.
  private LocalDate startDate; // Leave it as a string for now until we see what type of object we will use for the dates.
  private LocalDate endDate;  // same as startDate.

  public Task(String title, String description, LocalDate startDate, LocalDate endDate)
  {
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    assignedEmployees = new ArrayList<Employee>();
    priorityPoints = 0;
  }

  public void assignEmployee(Employee e)
  {
    assignedEmployees.add(e);
  }

  public void removeEmployee(Employee e)
  {
    assignedEmployees.remove(e);
  }

  public void setPriorityPoints(int points)
  {
    priorityPoints = points;
  }

  public void setStartDate(LocalDate startDate)
  {
    this.startDate = startDate;
  }

  public void setEndDate(LocalDate endDate)
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

  public ArrayList<Employee> getAssignedEmployees()
  {
    return assignedEmployees;
  }

  public Employee[] getAssignedEmployeesArray()
  {
    Employee[] employees = new Employee[assignedEmployees.size()];
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

  public LocalDate getEndDate()
  {
    return endDate;
  }

  public LocalDate getStartDate()
  {
    return startDate;
  }
}
