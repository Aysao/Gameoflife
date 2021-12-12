package Process;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Iserver extends Remote{
    
    public void Affichage() throws RemoteException;
    public void updateLifespan() throws RemoteException;
}
