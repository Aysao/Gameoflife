package Process;

import java.rmi.*;

public class Client {
    public Client(){
        
    }

    public static void main(String[] args)
    {
        try{
            Iserver srv = (Iserver) Naming.lookup("http://localhost:8000/GOL");
        }
        catch(Exception e ){
            e.printStackTrace();
        }
    }
}