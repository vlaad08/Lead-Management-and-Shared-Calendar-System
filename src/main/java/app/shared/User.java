package app.shared;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable
{

  private String firstName;
  private String middleName;
  private String lastName;
  private String email;
  private String phone;
  private boolean manager;
  private String street;
  private int postalCode;

  public User(String firstName, String middleName, String lastName, String email, String phone, boolean manager, String street, int postalCode) {
      this.firstName = firstName;
      this.middleName = middleName;
      this.lastName = lastName;
      this.email = email;
      this.phone = phone;
      this.manager = manager;
      this.street = street;
      this.postalCode = postalCode;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public void setPostalCode(int postalCode)
  {
    this.postalCode = postalCode;
  }

  public void setPhone(String phone)
  {
    this.phone = phone;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setMiddleName(String middleName)
  {
    this.middleName = middleName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPhone()
  {
    return phone;
  }

  public boolean isManager()
  {
    return manager;
  }
  public String toStringManager(){
    if(isManager()){
      return "Manager";
    }
    else{
      return "Employee";
    }
  }
  public String getStreet()
  {
    return street;
  }

  public int getPostalCode()
  {
    return postalCode;
  }

  public void setManager(boolean manager)
  {
    this.manager = manager;
  }

  public String toString()
  {
      return firstName + " " + lastName + " | " + email;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    return Objects.equals(email, other.email);
  }
}