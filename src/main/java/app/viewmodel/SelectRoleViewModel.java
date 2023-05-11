package app.viewmodel;

import app.model.Model;

public class SelectRoleViewModel
{
  private Model model;

  public SelectRoleViewModel(Model model)
  {
    this.model = model;
  }

  public void setUser()
  {
    model.setUser();
  }

}
