package app.shared;

import java.io.Serializable;

public class Lead implements Serializable
{
  private String firstname;
  private String middleName;
  private String lastname;
  private String email;
  private String phone;
  private String title;
  private int business_id;

  private String businessName;

  public Lead(String firstname, String middleName, String lastname, String email, String phone,String title, int business_id, String businessName)
  {
    this.firstname = firstname;
    this.middleName = middleName;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
    this.title = title;
    this.business_id = business_id;
    this.businessName = businessName;
  }

  public String getPhone()
  {
    return phone;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public String getFirstname()
  {
    return firstname;
  }

  public void setFirstname(String firstname)
  {
    this.firstname = firstname;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName(String middleName)
  {
    this.middleName = middleName;
  }

  public String getLastname()
  {
    return lastname;
  }

  public void setLastname(String lastname)
  {
    this.lastname = lastname;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public int getBusiness_id()
  {
    return business_id;
  }

  public void setBusiness_id(int business_id)
  {
    this.business_id = business_id;
  }

  public String toString()
  {
    return firstname + " " + lastname + " | " + email;

  }

  public void setBusinessName(String businessName)
  {
    this.businessName = businessName;
  }

  public String getBusinessName()
  {
    return businessName;
  }
}