package app.view;

import app.model.ConstraintChecker;
import app.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class LoginController
{
  private Region root;
  private ViewHandler viewHandler;
  private LoginViewModel loginViewModel;

  @FXML private TextField emailTextField;
  @FXML private TextField passwordField;

  Alert loginError  = new Alert(Alert.AlertType.ERROR);

  public void init(ViewHandler viewHandler, LoginViewModel loginViewModel,Region root)
  {
    this.root=root;
    this.loginViewModel = loginViewModel;
    this.viewHandler=viewHandler;

    
  }

  public Region getRoot()
  {
    return root;
  }


  public void logIn() throws SQLException, RemoteException
  {
    if(ConstraintChecker.checkFillOut(emailTextField) || ConstraintChecker.checkFillOut(passwordField))
    {
      if(loginViewModel.logIn(emailTextField.getText(), passwordField.getText()))
      {

        viewHandler.openView("Calendar");
      }
      else
      {
        loginError.setContentText("Email or password is incorrect");
        loginError.show();
      }
    }
    else
    {
      loginError.setContentText("Please fill out all the fields");
      loginError.show();
    }

  }

}
