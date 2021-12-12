package Process;

import java.io.Serializable;
import java.rmi.*;

public class Client implements Serializable{
    public Client(){
        
    }

    public static void main(String[] args)
    {
        try{
            Iserver srv = (Iserver) Naming.lookup("GOL");
            srv.Affichage();

            srv.updateLifespan(1,1,1);

            srv.Affichage();
            //srv.updateLifespan( srv.getCell(new Position(0, 1, 1)) );
            //srv.updateLifespan( srv.getCell(new Position(2, 1, 1)) );
        }
        catch(Exception e ){
            e.printStackTrace();
        }
    }
}
