package app;

import app.JDBC.SQLConnection;
import app.shared.User;
import app.shared.UserTableRow;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.util.ArrayList;

public class TableViewMain extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    ObservableList<UserTableRow> userTableRows;

    TableView<UserTableRow> tableView = new TableView<>();
    tableView.setEditable(true);
    TableColumn<UserTableRow, String> firstName = new TableColumn<>("First Name");
    TableColumn<UserTableRow, String> lastName = new TableColumn<>("Last Name");
    TableColumn<UserTableRow, String> email = new TableColumn<>("Email");
    TableColumn<UserTableRow, String> attends = new TableColumn<>("Attends");

    tableView.getColumns().add(firstName);
    tableView.getColumns().add(lastName);
    tableView.getColumns().add(email);
    tableView.getColumns().add(attends);

    firstName.setCellValueFactory(cell -> cell.getValue().firstNameProperty());
    lastName.setCellValueFactory(cell -> cell.getValue().lastNameProperty());
    email.setCellValueFactory(cell -> cell.getValue().emailProperty());
    attends.setCellValueFactory(cell -> cell.getValue().attendsProperty());

    attends.setCellFactory(ComboBoxTableCell.forTableColumn("Yes", "No"));


    ArrayList<User> users = SQLConnection.getInstance().getUsers();

    userTableRows = FXCollections.observableArrayList();
    for (User user : users) {
      userTableRows.add(new UserTableRow(user));
    }

    tableView.setItems(userTableRows);

    BorderPane borderPane = new BorderPane(tableView);
    Scene scene = new Scene(borderPane, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
