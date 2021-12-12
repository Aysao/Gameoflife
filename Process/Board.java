package Process;

public class Board {
	private Cell[][][] board;
	//TODO: index pour facilit� le passage de task a actualiser a chaque changement d'etat :  Hashmap ? 
	private Cell index[];
	
	
	public Board(int x, int y, int z)
	{
		board = new Cell[x][y][z];
		index = new Cell[x*y*z];
	}
	
	//retourne vrai si la position ne d�passe pas le plateau
	public boolean onBoard(Position p)
	{
		if(board.length < p.getX() && board.length > 0)
		{
			if(board[p.getX()].length < p.getY() && board[p.getX()].length > 0)
			{
				if(board[p.getX()][p.getY()].length < p.getZ() && board[p.getX()][p.getY()].length > 0)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//retourne vrai si la cellule cibl� est vivante
	public boolean isAlive(Position p)
	{
		if(board[p.getX()][p.getY()][p.getZ()].getState() == 1)
		{
			return true;
		}
		return false;
	}
	
	//initialisation de la cellule de depart
	public void initialize(int x, int y, int z)
	{
		Position p = new Position(x,y,z);
		if(onBoard(p))
		{
			board[x][y][z] = new Cell(p);
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
					if(i != 0 && j != 0 && h != 0)
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
	
	//apr�s une it�ration la cellule fait naitre les cellules voisine qui sont morte
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
							if(!isAlive(p))
							{
								board[p.getX()][p.getY()][p.getZ()] = new Cell(p);
							}
						}
					}
				}
			}
		}
	}
	
	//si la cellule c n'a pas assez de voisin elle meurt
	public void updateLifespan(Cell c)
	{
		if(getNbNear(c)[1] < 2 || getNbNear(c)[1] > 3 )
		{
			c.setState(0);
		}
		updateNear(c);
		//TODO : else augmenter sa lifespan pour un effet visuel
	}
}
