package app.shared;



import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Meeting implements Serializable
{
  private String title;
  private String description;
  private String leadEmail;
  private Date date;
  private Time startTime;
  private Time endTime;


  public Meeting(String title, String description, Date date, Time startTime, Time endTime, String leadEmail)
  {
    this.title = title;
    this.description = description;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.leadEmail = leadEmail;
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

  public String getLeadEmail()
  {
    return leadEmail;
  }

  public void setLeadEmail(String leadEmail)
  {
    this.leadEmail = leadEmail;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public void setStartTime(Time startTime)
  {
    this.startTime = startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public void setEndTime(Time endTime)
  {
    this.endTime = endTime;
  }

  @Override public String toString()
  {
    return "Meeting: " + title + " | Starts at " + startTime + " Ends at: " + endTime;
  }
}
