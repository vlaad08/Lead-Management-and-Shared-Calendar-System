package app.viewmodel;

import app.model.Model;
import app.shared.Meeting;

import java.util.ArrayList;

public class CalendarViewModel
{
  private Model model;

  public CalendarViewModel(Model model){
    this.model = model;
  }

  public ArrayList<Meeting> getMeetings()
  {
    return model.getMeetings();
  }

  //Im not doin all this
}
