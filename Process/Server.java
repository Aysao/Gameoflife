package Process;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class Server extends UnicastRemoteObject implements Iserver {

    private static Cell[][][] board;
	    //TODO: index pour facilite le passage de task a actualiser a chaque changement d'etat :  Hashmap ? 
	private static ArrayList<Cell> index;
	
    
    public Server() throws RemoteException{
        super();
        board = new Cell[3][3][3];
		Init();
        Initialize(0,1,1);
        Initialize(1,1,1);
        Initialize(2,1,1);
    }

	public void Init()
	{
		for(int i = 0; i <  board.length; i++)
		{
			for(int j = 0; j < board[i].length;j++)
			{
				for(int k = 0; k < board[i][j].length; k++)
				{
					board[i][j][k] = new Cell(i,j,k);
				}
			}
		}
	}

    public void Affichage()
    {
        System.out.println("tableau X Y");
        
		for(int k = 0; k < board[0][0].length; k++)
		{
			System.out.println(" Rang : " + k );
			for(int i = 0; i < board.length; i++)
			{
				System.out.print("[ ");
				for(int j = 0; j < board[i].length ;j++)
				{
					if(j != 0)
						System.out.print(" , " + board[i][j][k].toString());
					else
						System.out.print(board[i][j][k].toString());
				}
				System.out.println(" ]");
			}
			System.out.println("\n");
		}
		


        System.out.println("\n \n tableau Y Z");
        for(int i = 0; i < board.length; i++)
        {
            System.out.println(" Rang : " + i);
            for(int j = 0; j < board[i].length ;j++)
            {
                System.out.print("[ ");
                for(int k = 0; k < board[i][j].length ;k++)
                {
                    if(k != 0)
                        System.out.print(" , " + board[i][j][k].toString());
                    else
                        System.out.print(board[i][j][k].toString());
                }
                System.out.println(" ]");
            }
			System.out.println("\n");
        }


        System.out.println("\n \n tableau X Z");
        for(int j = 0; j < board[0].length; j++)
        {
            System.out.println(" Rang : " + j);
            for(int i = 0; i < board.length ;i++)
            {
                System.out.print("[ ");
                for(int k = 0; k < board[i][j].length ;k++)
                {
                    if(k != 0)
                        System.out.print(" , " + board[i][j][k].toString());
                    else
                        System.out.print(board[i][j][k].toString());
                }
                System.out.println(" ]");
            }
			System.out.println("\n");
        }
        
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
	
	//retourne vrai si la position ne depasse pas le plateau
	public boolean onBoard(Position p)
	{
		if(board.length > p.getX() && p.getX() >= 0)
		{
			if(board[p.getX()].length > p.getY() && p.getY() >= 0)
			{
				if(board[p.getX()][p.getY()].length > p.getZ() && p.getZ() >= 0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//retourne vrai si la cellule cible est vivante
	public boolean isAlive(Position p)
	{
		if(onBoard(p) && board[p.getX()][p.getY()][p.getZ()].getState() == 1)
		{
			return true;
		}
		return false;
	}
	
	//initialisation de la cellule de depart
	private void Initialize(int x, int y, int z)
	{
		Position p = new Position(x,y,z);
		if(onBoard(p))
		{
			System.out.println("Initialize ... : " + x + " / "+ y + " / "+ z);
			board[x][y][z].setState(1);
		}
	}

	//retourne un tableau des cellules voisines ( 0 pour les mort  / 1 pour les vivants )
	public int[] getNbNear(Cell c)
	{
		int[] k = new int[2];
		for(int i = -1; i < 2;i++)
		{	
			for(int j = -1; j < 2;j++)
			{
				for(int h = -1 ; h < 2; h++)
				{
					if( !(i == 0 && j == 0 && h == 0))
					{
						Position p = new Position(c.getPos().getX()+i, c.getPos().getY()+j, c.getPos().getZ()+h);
						if(onBoard(p))
						{
							if(!isAlive(p))
							{
								// les voisins qui ne sont pas en vie 
								k[0]++;
							}
							else
							{
								//les voisins en vie
								k[1]++;
							}
						}
					}
				}
			}
		}
		return k;
	}
	
	//apres une iteration la cellule fait naitre les cellules voisine qui sont morte
	public void updateNear(Cell c)
	{
		for(int i = -1; i < 2;i++)
		{	
			for(int j = -1; j < 2;j++)
			{
				for(int h = -1 ; h < 2; h++)
				{
					if(i != 0 && j != 0 && h != 0)
					{
						Position p = new Position(c.getPos().getX()+i, c.getPos().getY()+j, c.getPos().getZ()+h);
						if(onBoard(p))
						{
							if(!isAlive(p) && getNbNear(board[p.getX()][p.getY()][p.getZ()])[1] > 2)
							{
								//TODO : arrays list pour l'indexation pop push (BAG OF TASK)
								board[p.getX()][p.getY()][p.getZ()].setState(1);
							}
						}
					}
				}
			}
		}
	}
	
	//si la cellule c n'a pas assez de voisin elle meurt
	public void updateLifespan(int x,int y,int z)
	{
		Cell c = board[x][y][z];
		System.out.println(getNbNear(c)[1]);
		if(getNbNear(c)[1] < 2)
		{
			c.setState(0);
		}
		updateNear(c);
		//TODO : else augmenter sa lifespan pour un effet visuel

		//TODO : nbdevoisin a la creation
	}


    //TODO: BAG OF TASK GRACE A INDEXE

}
