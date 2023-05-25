package app.model;

import app.shared.User;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface ModelCalendar
{
  ArrayList<Object> getList(String expectedType);
  boolean isManager();
  void addPropertyChangeListener(PropertyChangeListener listener);
  User getLoggedInUser();
}
