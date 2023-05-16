package app.shared;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;


public class UserTableRow
{
  private final SimpleStringProperty firstName;
  private final SimpleStringProperty lastName;
  private final SimpleStringProperty email;

  private final SimpleStringProperty attends;


  private final User user;

  public UserTableRow(User user)
  {
    this.user = user;
    this.firstName = new SimpleStringProperty(user.getFirstName());
    this.lastName = new SimpleStringProperty(user.getLastName());
    this.email = new SimpleStringProperty(user.getEmail());
    this.attends = new SimpleStringProperty("Yes");
  }

  public String getFirstName()
  {
    return firstName.get();
  }

  public SimpleStringProperty firstNameProperty()
  {
    return firstName;
  }

  public void setFirstName(String firstName)
  {
    this.firstName.set(firstName);
  }

  public String getLastName()
  {
    return lastName.get();
  }

  public SimpleStringProperty lastNameProperty()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName.set(lastName);
  }

  public String getEmail()
  {
    return email.get();
  }

  public SimpleStringProperty emailProperty()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email.set(email);
  }

  public String getAttends()
  {
    return attends.get();
  }

  public SimpleStringProperty attendsProperty()
  {
    return attends;
  }

  public void setAttends(String attends)
  {
    this.attends.set(attends);
  }

  public User getUser()
  {
    return user;
  }
}
