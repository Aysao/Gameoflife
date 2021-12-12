package Process;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Iserver extends Remote{
    
    public void Affichage() throws RemoteException;
    public void updateLifespan(int x,int y,int z) throws RemoteException;
    public void updateNear(Cell c) throws RemoteException;
}
