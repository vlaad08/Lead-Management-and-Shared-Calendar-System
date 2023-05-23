package app.view;

import app.shared.User;
import app.viewmodel.AllUsersViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AllUsersController implements PropertyChangeListener
{
  public Button leadButton;
  @FXML private Button calendarButton;
  @FXML private Button tasksButton;
  @FXML private Button availableClientsButton;
  @FXML private Button meetingButton;

  @FXML private Button plansButton;
  @FXML private Button closeButton;
  @FXML private Button manageLeadsButton;
  @FXML private Button addUserButton;
  @FXML private VBox userVBox;

  @FXML private Label nameLabel;


  private Region root;
  private ViewHandler viewHandler;
  private AllUsersViewModel allUsersViewModel;

  private final ObjectProperty<ObservableList<User>> users = new SimpleObjectProperty<>();

  public void init(ViewHandler viewHandler, AllUsersViewModel allUsersViewModel, Region root){
    this.viewHandler = viewHandler;
    this.allUsersViewModel = allUsersViewModel;
    this.root = root;

    allUsersViewModel.addPropertyChangeListener(this);

    allUsersViewModel.bindUsers(users);
    allUsersViewModel.bindUserName(nameLabel.textProperty());

    Draw.hoverButtonNavbar(calendarButton, plansButton, meetingButton, tasksButton, availableClientsButton, manageLeadsButton, closeButton);


    Draw.drawUser(userVBox, users.get(), allUsersViewModel);

    userVBox.setPadding(new Insets(10));
    userVBox.setSpacing(15);





  }


  public void onCloseButton()
  {
    viewHandler.close();
  }

  public Region getRoot()
  {
    return root;
  }

  public void  reset(){} //why not

  @FXML public void changeView(ActionEvent e)
  {
    if(e.getSource().getClass() == Button.class)
    {
      Button b = (Button) e.getSource();

      switch (b.getText())
      {
        case "Calendar", "Plans" -> viewHandler.openView("Calendar");
        case "Manage meeting" -> viewHandler.openView("Meeting");
        case "Manage task" -> viewHandler.openView("Task");
        case "Lead", "Available Clients" -> viewHandler.openView("AvailableClients");
        case "All Users" -> viewHandler.openView("AllUsers");
        case "Manage leads" -> viewHandler.openView("Leads");
      }
    }
  }
//get Users
  public void addUser()
  {
    Draw.drawUserPopUp(userVBox, allUsersViewModel);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadUser"))
    {
      Draw.drawUser(userVBox, users.get(), allUsersViewModel);
    }
  }
}
