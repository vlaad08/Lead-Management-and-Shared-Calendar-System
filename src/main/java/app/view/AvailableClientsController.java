package app.view;

import app.shared.Lead;
import app.viewmodel.AvailableClientsViewModel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AvailableClientsController implements PropertyChangeListener
{

  @FXML private Button calendarButton;
  @FXML private Button tasksButton;
  @FXML private Button clientsButton;
  @FXML private Button meetingButton;
  @FXML private Button plansButton;
  @FXML private Button closeButton;
  @FXML private Button manageLeadsButton;
  @FXML private VBox availableVBox;

  private Region root;
  private ViewHandler viewHandler;
  private final ObjectProperty<ObservableList<Lead>> leads = new SimpleObjectProperty<>();

  public void init(ViewHandler viewHandler, AvailableClientsViewModel availableClientsViewModel, Region root){
    this.viewHandler = viewHandler;
    this.root = root;


    availableVBox.setPadding(new Insets(10));
    availableVBox.setSpacing(15);

    availableClientsViewModel.addPropertyChangeListener(this);

    Draw.hoverButtonNavbar(calendarButton, plansButton, meetingButton, tasksButton, clientsButton, manageLeadsButton, closeButton);
    availableClientsViewModel.bindLeads(leads);

    Draw.drawAvailableLeads(availableVBox, leads.get());
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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if(evt.getPropertyName().equals("reloadLeads"))
    {
      Platform.runLater(()-> Draw.drawAvailableLeads(availableVBox, leads.get()));
    }
  }
}
