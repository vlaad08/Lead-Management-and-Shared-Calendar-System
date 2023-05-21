package app.shared;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable
{

  private String street;
  private String city;
  private String country;
  private int postalCode;

  public Address(String street, String city, String country, int postalCode)
  {
    this.street = street;
    this.city = city;
    this.country = country;
    this.postalCode = postalCode;
  }

  public String getStreet()
  {
    return street;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public int getPostalCode()
  {
    return postalCode;
  }

  public void setPostalCode(int postalCode)
  {
    this.postalCode = postalCode;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Address address = (Address) o;
    return postalCode == address.postalCode && Objects.equals(street,
        address.street) && Objects.equals(city, address.city) && Objects.equals(
        country, address.country);
  }

  @Override public int hashCode()
  {
    return Objects.hash(street, city, country, postalCode);
  }
}
