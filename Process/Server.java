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
		index = new ArrayList<Cell>();
		Init();
        Initialize(0,1,1);
        Initialize(1,1,1);
        Initialize(2,1,1);
		Initialize(1,0,1);
        Initialize(1,2,1);
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
	public void getNbNear(Cell c)
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
		c.setNbVoisin(k[1]);
		if( ( c.getNbVoisin() > 4 && !isAlive(c.getPos()) ) || (c.getNbVoisin() < 4 && isAlive(c.getPos() ) ) )
		{
			index.add(c);
		}
	}
	
	//apres une iteration les cellules qui ont plus de 3 voisin ou plus et qui est morte devient vivante
	//si une cellule vivante a moins de deux voisin elle meurt sinon rien ne change
	public void updateNear()
	{
		for(int i = 0; i < index.size();i++)
		{
			Cell c = index.get(i);
			System.out.println(" index a modifier : " + i + " : " + c.getPos().getX() + " / " + c.getPos().getY() + " / " + c.getPos().getZ() + " State : " + c.getState());
			System.out.println(" index nb voisin " + c.getNbVoisin());
			if(c.getState() == 1 && c.getNbVoisin() < 4)
			{
				board[c.getPos().getX()][c.getPos().getY()][c.getPos().getZ()].setState(0);
			}
			else if(c.getState() == 0 && c.getNbVoisin() > 4)
			{
				board[c.getPos().getX()][c.getPos().getY()][c.getPos().getZ()].setState(1);
			}
		}
		index.clear();
	}
	
	//si la cellule c n'a pas assez de voisin elle meurt
	public void updateLifespan()
	{
		for(int x = 0; x < board.length;x++)
		{
			for(int y = 0;y < board[x].length;y++)
			{
				for(int z = 0; z < board[x][y].length; z++)
				{
					getNbNear(board[x][y][z]);
				}
			}
		} 
		updateNear();
	}


    //TODO: BAG OF TASK GRACE A INDEXE

}
