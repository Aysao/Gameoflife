package Process;

import java.io.Serializable;

public class Cell implements Serializable {
	private Position pos;
	private int state = 0;
	private int nbVoisin = 0;
	
	public Cell(int posX, int posY, int posZ)
	{
		pos = new Position(posX,posY,posZ);
	}


	public Cell(Position p)
	{
		pos = p;
	}
	public Cell()
	{
		this(-1, -1 , -1);
	}
	
	public int getState() {
		return state;
	}
	public Position getPos() {
		return pos;
	}
	
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String toString()
	{
		if(state == 1)
		{
			return "Alive";
		}
		else
		{
			return "Dead";
		}
	}
	public int getNbVoisin() {
		return nbVoisin;
	}
	public void setNbVoisin(int nbVoisin) {
		this.nbVoisin = nbVoisin;
	}
}
