package Process;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;


public class Server extends UnicastRemoteObject implements Iserver {

    public Server() throws RemoteException{
        super();
    }

    public void Affichage()
    {
        System.out.println("Coucou");
    }

    public static void main(String[] args)
    {
        try {
            Server srv = new Server();
            Naming.rebind("GOL", srv);
            System.out.println("Demarrage du serveur ... ");

        } catch (Exception e) {
            System.out.println("Erreur ! ");
            e.printStackTrace();
        }
    }


}
