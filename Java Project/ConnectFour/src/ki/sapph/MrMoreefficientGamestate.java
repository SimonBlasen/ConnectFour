package ki.sapph;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;

public class MrMoreefficientGamestate 
{
	public SogoGame rootGame;
	public boolean isMax;
	private double alpha;
	private double beta;
	private double value;
	private boolean valueSet = false;
	
	public boolean isPruned = false;
	
	private boolean hasEnded = false;
	
	private int movePos = -1;

	/**
	 * 
	 * b1 indicates whether the field is air or not.
	 * False means air, True means not
	 * 
	 * b2 indicates the player having set a value. False is P1, True is P2
	 * 
	 * 
	 */
	
	
	
	private boolean[][][] b1s = new boolean[4][4][4];
	private boolean[][][] b2s = new boolean[4][4][4];
	
	private MrMoreefficientGamestate[] children = new MrMoreefficientGamestate[] {null, null, null,null,null,null,null,null,null,null,null,null,null,null,null,null};
	private MrMoreefficientGamestate parent = null;
	
	
	public MrMoreefficientGamestate()
	{
		this(true, 0.0);
	}
	
	public MrMoreefficientGamestate(boolean isMax)
	{
		this(isMax, 0.0);
	}
	
	public MrMoreefficientGamestate(boolean isMax, double value)
	{
		this.isMax = isMax;
		this.value = value;
		
		this.alpha = -1000000.0;
		this.beta = 1000000.0;
		
		//children = new ArrayList<MrMoreefficientGamestate>();
	}
	
	
	public double getValue()
	{
		if (valueSet == false)
		{
			return isMax ? 10000.0 : -10000.0;
		}
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
		valueSet = true;
	}
	
	public boolean isValueSet()
	{
		return valueSet;
	}
	
	/**
	 * If a child node found a new value, which is possible to achieve
	 * @param value
	 * @return Whether the value helped or not
	 */
	public boolean updateValue(double value)
	{
		if (isMax && value > alpha)
		{
			alpha = value;
		}
		else if (isMax == false && value < beta)
		{
			beta = value;
		}
		
		if (valueSet == false || (value > this.value == isMax))
		{
			setValue(value);
			return true;
		}
		return false;
	}
	
	public boolean fieldAir(int x, int y, int z)
	{
		return b1s[x][y][z] == false;
	}
	
	public boolean fieldAir(int[] coords)
	{
		return fieldAir(coords[0], coords[1], coords[2]);
	}
	
	public boolean fieldP(int x, int y, int z, boolean player)
	{
		return b2s[x][y][z] == player;
	}
	
	public boolean fieldP(int[] coords, boolean player)
	{
		return fieldP(coords[0], coords[1], coords[2], player);
	}
	
	public boolean canPrun()
	{
		return alpha >= beta;
	}
	
	public boolean doesHelp(double value)
	{
		if (valueSet == false)
		{
			return true;
		}
		
		return value > this.value == isMax;
	}
	
	public MrMoreefficientGamestate getParent()
	{
		return parent;
	}
	
	public void applyGame(SogoGame game)
	{
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					if (game.board[x][y][z] == Player.NONE)
					{
						b1s[x][y][z] = false;
					}
					else
					{
						b1s[x][y][z] = true;
						b2s[x][y][z] = game.board[x][y][z] != game.getCurrentPlayer();
					}
				}
			}
		}
	}
	
	public void setParent(MrMoreefficientGamestate parent, int moveIndex)
	{
		this.parent = parent;
		this.alpha = parent.alpha;
		this.beta = parent.beta;

		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					this.b1s[x][y][z] = parent.b1s[x][y][z];
					this.b2s[x][y][z] = parent.b2s[x][y][z];
				}
			}
		}
		
		if (this.parent != null)
		{
			this.parent.children[moveIndex] = this;
			this.rootGame = parent.rootGame;
			movePos = moveIndex;
			
			int x = moveIndex % 4;
			int y = moveIndex / 4;
			int z = 0;
			
			for (int i = 0; i < 4; i++)
			{
				if (b1s[x][y][i] == false)
				{
					z = i;
					b1s[x][y][i] = true;
					b2s[x][y][i] = isMax;
					break;
				}
			}
			
			
			boolean winX = true;
			boolean winY = true;
			boolean winZ = true;
			boolean winCX1 = true;
			boolean winCY1 = true;
			boolean winCZ1 = true;
			boolean winCX2 = true;
			boolean winCY2 = true;
			boolean winCZ2 = true;

			boolean winQ1 = true;
			boolean winQ2 = true;
			boolean winQ3 = true;
			boolean winQ4 = true;
			for (int s = 0; s < 4; s++)
			{
				if (winX && (b1s[s][y][z] == false || b2s[s][y][z] != isMax))
					winX = false;
				if (winY && (b1s[x][s][z] == false || b2s[x][s][z] != isMax))
					winY = false;
				if (winZ && (b1s[x][y][s] == false || b2s[x][y][s] != isMax))
					winZ = false;
				

				if (winCX1 && (b1s[x][s][s] == false || b2s[x][s][s] != isMax))
					winCX1 = false;
				if (winCY1 && (b1s[s][y][s] == false || b2s[s][y][s] != isMax))
					winCY1 = false;
				if (winCZ1 && (b1s[s][s][z] == false || b2s[s][s][z] != isMax))
					winCZ1 = false;
				if (winCX2 && (b1s[x][3-s][s] == false || b2s[x][3-s][s] != isMax))
					winCX2 = false;
				if (winCY2 && (b1s[3-s][y][s] == false || b2s[3-s][y][s] != isMax))
					winCY2 = false;
				if (winCZ2 && (b1s[3-s][s][z] == false || b2s[3-s][s][z] != isMax))
					winCZ2 = false;
				

				if (winQ1 && (b1s[s][s][s] == false || b2s[s][s][s] != isMax))
					winQ1 = false;
				if (winQ2 && (b1s[s][s][3-s] == false || b2s[s][s][3-s] != isMax))
					winQ2 = false;
				if (winQ3 && (b1s[3-s][s][s] == false || b2s[3-s][s][s] != isMax))
					winQ3 = false;
				if (winQ4 && (b1s[3-s][s][3-s] == false || b2s[3-s][s][3-s] != isMax))
					winQ4 = false;
			}
			
			hasEnded = winX || winY || winZ || winCX1 || winCX2 || winCY1 || winCY2 || winCZ1 || winCZ2 || winQ1 || winQ2 || winQ3 || winQ4;
			
			if (hasEnded)
			{
				//System.out.println("Has ended");
			}
		}
	}
	
	public boolean hasEnded()
	{
		return hasEnded;
	}
	
	public boolean canMoveAt(int index)
	{
		return canMoveAt(index % 4, index / 4);
	}
	
	public boolean canMoveAt(int x, int y)
	{
		return b1s[x][y][3] == false;
	}
	
	public MrMoreefficientGamestate[] getChildren()
	{
		return children;
	}
}
