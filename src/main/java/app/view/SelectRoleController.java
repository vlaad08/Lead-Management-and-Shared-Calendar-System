package app.view;

import app.viewmodel.SelectRoleViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class SelectRoleController
{
  private Region root;
  private ViewHandler viewHandler;
  private SelectRoleViewModel selectRoleViewModel;

  @FXML private Button managerButton;
  @FXML private Button employeeButton;

  public void init(ViewHandler viewHandler, SelectRoleViewModel selectRoleViewModel,Region root)
  {
    this.root=root;
    this.selectRoleViewModel=selectRoleViewModel;
    this.viewHandler=viewHandler;
  }

  public Region getRoot()
  {
    return root;
  }

  public void onManagerButton()
  {
    selectRoleViewModel.setUser();
    viewHandler.openView("Calendar");
  }
  public void onEmployeeButton(){
    viewHandler.openView("Calendar");
  }


}
