package app.server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class StartServer
{
  public static void main(String[] args) throws Exception
  {
<<<<<<< Updated upstream
    Registry registry = LocateRegistry.createRegistry(1099);
    ServerImplementation communicator = new ServerImplementation();
    Remote remote = UnicastRemoteObject.exportObject(communicator,8888);
=======
    Registry registry = LocateRegistry.createRegistry(7430);
    ServerImplementation communicator = new ServerImplementation();
    Remote remote = UnicastRemoteObject.exportObject(communicator,0);
>>>>>>> Stashed changes
    registry.bind("communicator", remote);
  }

}
