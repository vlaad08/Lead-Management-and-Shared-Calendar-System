package app.view;

import app.shared.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class UserTableRow
{
  private final SimpleStringProperty firstName;
  private final SimpleStringProperty lastName;
  private final SimpleStringProperty email;

  private final SimpleStringProperty attends;

  public UserTableRow(User user)
  {
    this.firstName = new SimpleStringProperty(user.getFirstName());
    this.lastName = new SimpleStringProperty(user.getLastName());
    this.email = new SimpleStringProperty(user.getEmail());
    this.attends = new SimpleStringProperty("Yes");
  }


  public SimpleStringProperty firstNameProperty()
  {
    return firstName;
  }


  public SimpleStringProperty lastNameProperty()
  {
    return lastName;
  }

  public SimpleStringProperty emailProperty()
  {
    return email;
  }

  public ObservableValue<String> attendsProperty()
  {
    return attends;
  }

  public String getEmail()
  {
    return email.getValueSafe();
  }
}
