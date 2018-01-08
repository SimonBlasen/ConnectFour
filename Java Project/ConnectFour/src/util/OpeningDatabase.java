package util;

import java.util.Random;

import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.game.SogoMove;

public class OpeningDatabase {

	private static OpeningStrategy strategy = OpeningStrategy.DIAGONAL_FORCATION;
	
	
	public static SogoMove getMove(SogoGame g, Player p)
	{
		switch (strategy) {
		case DIAGONAL_FORCATION:
			
			int stones = countStones(g);
			
			switch (stones) {
			case 0:
				SogoMove[] tMoves = new SogoMove[] {new SogoMove(0, 1), new SogoMove(0, 2), new SogoMove(1, 0), new SogoMove(2, 0), new SogoMove(3, 1), new SogoMove(3, 2), new SogoMove(1, 3), new SogoMove(2, 3)};
				
				return tMoves[(new Random()).nextInt(tMoves.length)];
			case 2:
				int pos[] = new int[3];
				int posOther[] = new int[3];
				
				for (int x = 0; x < 4; x++)
				{
					for (int y = 0; y < 4; y++)
					{
						for (int z = 0; z < 4; z++)
						{
							if (g.board[x][y][z] == p)
							{
								pos[0] = x;
								pos[1] = y;
								pos[2] = z;
							}
							if (g.board[x][y][z] == (p == Player.P1 ? Player.P2 : Player.P1))
							{
								posOther[0] = x;
								posOther[1] = y;
								posOther[2] = z;
							}
						}
					}
				}
				
				if (!((posOther[0] == 0 || posOther[0] == 3) && (posOther[1] == 0 || posOther[1] == 3)))
				{
					return null;
				}
				else
				{
					posOther[0] = 3 - posOther[0];
					posOther[1] = 3 - posOther[1];
					
					int[] tPos = new int[3];
					
					if (pos[0] == 0)
					{
						tPos[0] = 0;
						
						if (posOther[0] == 0)
						{
							tPos[1] = 3 - posOther[1];
						}
						else
						{
							return null;
						}
					}
					else if (pos[0] == 3)
					{
						tPos[0] = 3;
						
						if (posOther[0] == 3)
						{
							tPos[1] = 3 - posOther[1];
						}
						else
						{
							return null;
						}
					}
					else if (pos[1] == 0)
					{
						tPos[1] = 0;
						
						if (posOther[1] == 0)
						{
							tPos[0] = 3 - posOther[0];
						}
						else
						{
							return null;
						}
					}
					else if (pos[1] == 3)
					{
						tPos[1] = 3;
						
						if (posOther[1] == 3)
						{
							tPos[0] = 3 - posOther[0];
						}
						else
						{
							return null;
						}
					}
					else
					{
						return null;
					}
					
					return new SogoMove(tPos[0], tPos[1]);
				}
			case 4:
				
				int pos2[] = new int[3];
				
				for (int x = 0; x < 4; x++)
				{
					for (int y = 0; y < 4; y++)
					{
						for (int z = 0; z < 4; z++)
						{
							if (g.board[x][y][z] == p)
							{
								pos2[0] = x;
								pos2[1] = y;
								pos2[2] = z;
							}
						}
					}
				}

				if ((pos2[0] == 0 || pos2[0] == 3) && (pos2[1] == 0 || pos2[1] == 3))
				{
					if (g.board[3 - pos2[0]][3 - pos2[1]][0] == Player.NONE)
					{
						return new SogoMove(3 - pos2[0], 3 - pos2[1]);
					}
					else
					{
						return null;
					}
				}
				else
				{
					return null;
				}
				
				
				
			case 1:
				
				int[] posOther2 = new int[3];
				
				for (int x = 0; x < 4; x++)
				{
					for (int y = 0; y < 4; y++)
					{
						for (int z = 0; z < 4; z++)
						{
							if (g.board[x][y][z] == (p == Player.P1 ? Player.P2 : Player.P1))
							{
								posOther2[0] = x;
								posOther2[1] = y;
								posOther2[2] = z;
							}
						}
					}
				}

				if ((posOther2[0] == 0 || posOther2[0] == 3) && (posOther2[1] == 0 || posOther2[1] == 3))
				{
					SogoMove[] tMovesIndexToPreventShittystiwchcasestuff = new SogoMove[] {new SogoMove(3 - posOther2[0], posOther2[1]), new SogoMove(posOther2[0], 3 - posOther2[1])};
					
					
					return tMovesIndexToPreventShittystiwchcasestuff[(new Random()).nextInt(tMovesIndexToPreventShittystiwchcasestuff.length)];
				}
				else
				{
					return null;
				}
				
				
			case 3:
				
				int pos3[] = new int[3];
				
				for (int x = 0; x < 4; x++)
				{
					for (int y = 0; y < 4; y++)
					{
						for (int z = 0; z < 4; z++)
						{
							if (g.board[x][y][z] == p)
							{
								pos3[0] = x;
								pos3[1] = y;
								pos3[2] = z;
							}
						}
					}
				}

				if ((pos3[0] == 0 || pos3[0] == 3) && (pos3[1] == 0 || pos3[1] == 3))
				{
					if (g.board[3 - pos3[0]][3 - pos3[1]][0] == Player.NONE)
					{
						return new SogoMove(3 - pos3[0], 3 - pos3[1]);
					}
					else
					{
						return null;
					}
				}
				else
				{
					return null;
				}
				
				
				
				
			default:


				return null;
			}
			

		default:
			return null;
			
		}
	}
	
	
	public static void setStrategy(OpeningStrategy strategy) {
		OpeningDatabase.strategy = strategy;
	}
	
	
	private static int countStones(SogoGame g)
	{
		int stonesAmount = 0;
		
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					if (g.board[x][y][z] == Player.P1)
					{
						stonesAmount++;
					}
					else if (g.board[x][y][z] == Player.P2)
					{
						stonesAmount++;
					}
				}
			}
		}
		
		return stonesAmount;
	}
}
