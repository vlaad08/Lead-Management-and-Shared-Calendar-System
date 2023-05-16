package app.server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class StartServer
{
  public static void main(String[] args) throws Exception
  {
    ServerImplementation communicator = new ServerImplementation();
    Registry registry = LocateRegistry.createRegistry(7000);
    Remote remote = UnicastRemoteObject.exportObject(communicator,0);
    registry.bind("communicator", remote);
  }

}
