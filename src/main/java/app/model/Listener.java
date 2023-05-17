package app.model;


import dk.via.remote.observer.RemotePropertyChangeEvent;
import dk.via.remote.observer.RemotePropertyChangeListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Listener extends UnicastRemoteObject implements
    RemotePropertyChangeListener<String>
{
  private final ReloadData model;

  public Listener(ReloadData model) throws RemoteException{
    this.model = model;

  }

  @Override public void propertyChange(
      RemotePropertyChangeEvent<String> remotePropertyChangeEvent)
      throws RemoteException
  {
    try{
      if(remotePropertyChangeEvent.getPropertyName().equals("reloadMeeting")){
        model.meetingAddedFromServer();
      }else if(remotePropertyChangeEvent.getPropertyName().equals("reloadTask")){
        model.taskAddedFromServer();
      }else if(remotePropertyChangeEvent.getPropertyName().equals("reloadLead")){
        model.leadAddedFromServer();
      }
    }catch (Exception e){
      e.printStackTrace();
    }

  }
}
