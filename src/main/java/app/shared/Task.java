package app.shared;

import java.io.Serializable;
import java.sql.Date;

public class Task implements Serializable
{

  private String title;
  private String description;
  private Date date;
  private String status;
  private final int business_id;

  public Task(String title, String description, Date date, String status, int business_id)
  {
    this.title = title;
    this.description = description;
    this.date = date;
    this.status = status;
    this.business_id = business_id;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public int getBusiness_id()
  {
    return business_id;
  }

  public String toString()
  {
    return "Task: " + title + " | Due Date: " + date;
  }
}
