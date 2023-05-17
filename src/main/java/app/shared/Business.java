package app.shared;

import java.io.Serializable;

public class Business implements Serializable
{
  private String name;
  private int business_id;
  private String street;
  private int postalCode;

  public Business(String name, int business_id, String street, int postalCode)
  {
    this.name = name;
    this.business_id = business_id;
    this.street = street;
    this.postalCode = postalCode;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public int getBusiness_id()
  {
    return business_id;
  }

  public void setBusiness_id(int business_id)
  {
    this.business_id = business_id;
  }

  public String getStreet()
  {
    return street;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public int getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode(int postalCode)
  {
    this.postalCode = postalCode;
  }

  @Override public String toString()
  {
    return name;
  }
}
