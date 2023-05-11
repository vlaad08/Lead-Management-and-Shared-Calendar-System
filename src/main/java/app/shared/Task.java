package app.shared;

import app.model.User;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

public record Task(String title, String description, Date date, String status, int business_id) implements Serializable
{
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

  public String toString(){
    return title+"\ndescription: "+description+"\ndate: "+date.toString()+
        "\nstatus: "+status+"business: "+business_id+"\n";
  }
}
