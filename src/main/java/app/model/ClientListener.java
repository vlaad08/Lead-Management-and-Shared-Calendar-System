package app.model;

import app.server.Server;
import app.shared.Meeting;
import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientListener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<String>
{
  private final Server server;
  private PropertyChangeSupport support;

  public ClientListener(Server server)throws RemoteException{
    this.server = server;
    server.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  public void addMeeting(Meeting meeting)throws RemoteException{
    server.addMeeting(meeting);
  }

  public ArrayList<Meeting> getMeetings()throws RemoteException{
    return server.getMeetings();
  }

  public void propertyChange(RemotePropertyChangeEvent<String> event) throws RemoteException
  {
    if(event.getPropertyName().equals("meeting")){
      support.firePropertyChange("meeting",null,"");
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener listener){
    support.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener){
    support.removePropertyChangeListener(listener);
  }

}
