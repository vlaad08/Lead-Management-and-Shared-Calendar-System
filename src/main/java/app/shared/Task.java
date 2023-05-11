package app.shared;

import app.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Serializable
{ private String title;
  private String description;
  private Date date;
  private String status;
  private int business_id;

  private ArrayList<User> assignedEmployees; //This would be for the assigned employees to complete the task. It could also be left empty.

  public Task(String title, String description, Date date, String status, int business_id){
    this.title = title;
    this.description = description;
    this.date = date;
    this.business_id = business_id;
  }

  public void assignEmployee(User e)
  {
    assignedEmployees.add(e);
  }

  public void removeEmployee(User e)
  {
    assignedEmployees.remove(e);
  }

  public void setTitle(String title)
  {
    this.title = title;
 }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setDate(Date date)
  {
    this.date = date;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public void setBusiness_id(int business_id)
  {
    this.business_id = business_id;
  }

  public String getTitle()
  {
    return title;
  }
  public String getDescription()
  {
    return description;
  }

  public Date getDate()
  {
    return date;
  }

  public String getStatus()
  {
    return status;
  }

  public int getBusinessId()
  {
    return business_id;
  }





  public ArrayList<User> getAssignedEmployees()
  {
    return assignedEmployees;
  }




}
