package util;

import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;

public class GameAnalyzer {

	
	public static final int[][][] lines = new int[][][] {{{0,0,0},{0,0,1},{0,0,2},{0,0,3}},
		{{1,0,0},{1,0,1},{1,0,2},{1,0,3}},
		{{2,0,0},{2,0,1},{2,0,2},{2,0,3}},
		{{3,0,0},{3,0,1},{3,0,2},{3,0,3}},
		{{0,1,0},{0,1,1},{0,1,2},{0,1,3}},
		{{1,1,0},{1,1,1},{1,1,2},{1,1,3}},
		{{2,1,0},{2,1,1},{2,1,2},{2,1,3}},
		{{3,1,0},{3,1,1},{3,1,2},{3,1,3}},
		{{0,2,0},{0,2,1},{0,2,2},{0,2,3}},
		{{1,2,0},{1,2,1},{1,2,2},{1,2,3}},
		{{2,2,0},{2,2,1},{2,2,2},{2,2,3}},
		{{3,2,0},{3,2,1},{3,2,2},{3,2,3}},
		{{0,3,0},{0,3,1},{0,3,2},{0,3,3}},
		{{1,3,0},{1,3,1},{1,3,2},{1,3,3}},
		{{2,3,0},{2,3,1},{2,3,2},{2,3,3}},
		{{3,3,0},{3,3,1},{3,3,2},{3,3,3}},
		
		{{0,0,0},{0,1,0},{0,2,0},{0,3,0}},
		{{1,0,0},{1,1,0},{1,2,0},{1,3,0}},
		{{2,0,0},{2,1,0},{2,2,0},{2,3,0}},
		{{3,0,0},{3,1,0},{3,2,0},{3,3,0}},
		{{0,0,1},{0,1,1},{0,2,1},{0,3,1}},
		{{1,0,1},{1,1,1},{1,2,1},{1,3,1}},
		{{2,0,1},{2,1,1},{2,2,1},{2,3,1}},
		{{3,0,1},{3,1,1},{3,2,1},{3,3,1}},
		{{0,0,2},{0,1,2},{0,2,2},{0,3,2}},
		{{1,0,2},{1,1,2},{1,2,2},{1,3,2}},
		{{2,0,2},{2,1,2},{2,2,2},{2,3,2}},
		{{3,0,2},{3,1,2},{3,2,2},{3,3,2}},
		{{0,0,3},{0,1,3},{0,2,3},{0,3,3}},
		{{1,0,3},{1,1,3},{1,2,3},{1,3,3}},
		{{2,0,3},{2,1,3},{2,2,3},{2,3,3}},
		{{3,0,3},{3,1,3},{3,2,3},{3,3,3}},
		
		{{0,0,0},{1,0,0},{2,0,0},{3,0,0}},
		{{0,1,0},{1,1,0},{2,1,0},{3,1,0}},
		{{0,2,0},{1,2,0},{2,2,0},{3,2,0}},
		{{0,3,0},{1,3,0},{2,3,0},{3,3,0}},
		{{0,0,1},{1,0,1},{2,0,1},{3,0,1}},
		{{0,1,1},{1,1,1},{2,1,1},{3,1,1}},
		{{0,2,1},{1,2,1},{2,2,1},{3,2,1}},
		{{0,3,1},{1,3,1},{2,3,1},{3,3,1}},
		{{0,0,2},{1,0,2},{2,0,2},{3,0,2}},
		{{0,1,2},{1,1,2},{2,1,2},{3,1,2}},
		{{0,2,2},{1,2,2},{2,2,2},{3,2,2}},
		{{0,3,2},{1,3,2},{2,3,2},{3,3,2}},
		{{0,0,3},{1,0,3},{2,0,3},{3,0,3}},
		{{0,1,3},{1,1,3},{2,1,3},{3,1,3}},
		{{0,2,3},{1,2,3},{2,2,3},{3,2,3}},
		{{0,3,3},{1,3,3},{2,3,3},{3,3,3}},
		
	
	

		{{0,0,0},{0,1,1},{0,2,2},{0,3,3}},
		{{1,0,0},{1,1,1},{1,2,2},{1,3,3}},
		{{2,0,0},{2,1,1},{2,2,2},{2,3,3}},
		{{3,0,0},{3,1,1},{3,2,2},{3,3,3}},
		{{0,0,3},{0,1,2},{0,2,1},{0,3,0}},
		{{1,0,3},{1,1,2},{1,2,1},{1,3,0}},
		{{2,0,3},{2,1,2},{2,2,1},{2,3,0}},
		{{3,0,3},{3,1,2},{3,2,1},{3,3,0}},

		{{0,0,0},{1,0,1},{2,0,2},{3,0,3}},
		{{0,1,0},{1,1,1},{2,1,2},{3,1,3}},
		{{0,2,0},{1,2,1},{2,2,2},{3,2,3}},
		{{0,3,0},{1,3,1},{2,3,2},{3,3,3}},
		{{0,0,3},{1,0,2},{2,0,1},{3,0,0}},
		{{0,1,3},{1,1,2},{2,1,1},{3,1,0}},
		{{0,2,3},{1,2,2},{2,2,1},{3,2,0}},
		{{0,3,3},{1,3,2},{2,3,1},{3,3,0}},

		{{0,0,0},{1,1,0},{2,2,0},{3,3,0}},
		{{0,0,1},{1,1,1},{2,2,1},{3,3,1}},
		{{0,0,2},{1,1,2},{2,2,2},{3,3,2}},
		{{0,0,3},{1,1,3},{2,2,3},{3,3,3}},
		{{3,0,0},{2,1,0},{1,2,0},{0,3,0}},
		{{3,0,1},{2,1,1},{1,2,1},{0,3,1}},
		{{3,0,2},{2,1,2},{1,2,2},{0,3,2}},
		{{3,0,3},{2,1,3},{1,2,3},{0,3,3}},
		
	

		{{0,0,0},{1,1,1},{2,2,2},{3,3,3}},
		{{0,0,3},{1,1,2},{2,2,1},{3,3,0}},
		{{3,0,0},{2,1,1},{1,2,2},{0,3,3}},
		{{3,0,3},{2,1,2},{1,2,1},{0,3,0}},};
		
		public static final long[] longLines = new long[] {0x000000000000000F,
				0x00000000000000F0,
				0x0000000000000F00,
				0x000000000000F000,
				0x00000000000F0000,
				0x0000000000F00000,
				0x000000000F000000,
				0x00000000F0000000,
				0x0000000F00000000L,
				0x000000F000000000L,
				0x00000F0000000000L,
				0x0000F00000000000L,
				0x000F000000000000L,
				0x00F0000000000000L,
				0x0F00000000000000L,
				0xF000000000000000L,

				0x0000000000001111L,
				0x0000000000002222L,
				0x0000000000004444L,
				0x0000000000008888L,
				0x0000000011110000L,
				0x0000000022220000L,
				0x0000000044440000L,
				0x0000000088880000L,
				0x0000111100000000L,
				0x0000222200000000L,
				0x0000444400000000L,
				0x0000888800000000L,
				0x1111000000000000L,
				0x2222000000000000L,
				0x4444000000000000L,
				0x8888000000000000L,

				0x0001000100010001L,
				0x0002000200020002L,
				0x0004000400040004L,
				0x0008000800080008L,
				0x0010001000100010L,
				0x0020002000200020L,
				0x0040004000400040L,
				0x0080008000800080L,
				0x0100010001000100L,
				0x0200020002000200L,
				0x0400040004000400L,
				0x0800080008000800L,
				0x1000100010001000L,
				0x2000200020002000L,
				0x4000400040004000L,
				0x8000800080008000L,
				
				
				
				

				0x0008000400020001L,
				0x0080004000200010L,
				0x0800040002000100L,
				0x8000400020001000L,
				0x0001000200040008L,
				0x0010002000400080L,
				0x0100020004000800L,
				0x1000200040008000L,

				0x1000010000100001L,
				0x2000020000200002L,
				0x4000040000400004L,
				0x8000080000800008L,
				0x0001001001001000L,
				0x0002002002002000L,
				0x0004004004004000L,
				0x0008008008008000L,

				0x0000000000008421L,
				0x0000000084210000L,
				0x0000842100000000L,
				0x8421000000000000L,
				0x0000000000001248L,
				0x0000000012480000L,
				0x0000124800000000L,
				0x1248000000000000L,
				
				
				

				0x8000040000200001L,
				0x0001002004008000L,
				0x1000020000400008L,
				0x0008004002001000L,
				};
	
	public static int countFreeLine(Player[] line, Player player) {
		int c = 0;
		for (int i = 0; i < 4; i++) {
			if (line[i] == player) {
				c++;
			}
			else if (line[i] != Player.NONE)
			{
				return 0;
			}
		}
		return c;
	}
	
	
	public static boolean hasGameEnded(long bp1)
	{
		for (int i = 0; i < longLines.length; i++)
		{
			if ((bp1 & longLines[i]) == longLines[i])
			{
				return true;
			}
		}
		return false;
	}
	
	
	public static long getBP1FromGame(SogoGame g)
	{
		long bp = 0x0L;
		for (int z = 3; z >= 0; z--)
		{
			for (int y = 3; y >= 0; y--)
			{
				for (int x = 3; x >= 0; x--)
				{
					bp = (bp << 1) | (g.board[x][y][z] == g.getCurrentPlayer() ? 0x1L : 0x0L);
				}
			}
		}
		
		return bp;
	}
	
	public static long getBP2FromGame(SogoGame g)
	{
		long bp = 0x0L;
		for (int z = 3; z >= 0; z--)
		{
			for (int y = 3; y >= 0; y--)
			{
				for (int x = 3; x >= 0; x--)
				{
					bp = (bp << 1) | (g.board[x][y][z] == g.getOtherPlayer() ? 0x1L : 0x0L);
				}
			}
		}
		
		return bp;
	}
	
	public static boolean[][][] getB1sFromGame(SogoGame g)
	{
		boolean[][][] b1s = new boolean[4][4][4];
		
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					if (g.board[x][y][z] == Player.NONE)
					{
						b1s[x][y][z] = false;
					}
					else
					{
						b1s[x][y][z] = true;
					}
				}
			}
		}
		
		return b1s;
	}
	
	public static boolean[][][] getB2sFromGame(SogoGame g)
	{
		boolean[][][] b2s = new boolean[4][4][4];
		
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					if (g.board[x][y][z] == Player.NONE)
					{

					}
					else
					{
						b2s[x][y][z] = g.board[x][y][z] != g.getCurrentPlayer();
					}
				}
			}
		}
		
		return b2s;
	}
	
	public static boolean hasGameEnded(boolean[][][] b1s, boolean[][][] b2s, int x, int y, int z, boolean whoCouldHaveEndedIt)
	{
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
			if (winX && (b1s[s][y][z] == false || b2s[s][y][z] != whoCouldHaveEndedIt))
				winX = false;
			if (winY && (b1s[x][s][z] == false || b2s[x][s][z] != whoCouldHaveEndedIt))
				winY = false;
			if (winZ && (b1s[x][y][s] == false || b2s[x][y][s] != whoCouldHaveEndedIt))
				winZ = false;
			

			if (winCX1 && (b1s[x][s][s] == false || b2s[x][s][s] != whoCouldHaveEndedIt))
				winCX1 = false;
			if (winCY1 && (b1s[s][y][s] == false || b2s[s][y][s] != whoCouldHaveEndedIt))
				winCY1 = false;
			if (winCZ1 && (b1s[s][s][z] == false || b2s[s][s][z] != whoCouldHaveEndedIt))
				winCZ1 = false;
			if (winCX2 && (b1s[x][3-s][s] == false || b2s[x][3-s][s] != whoCouldHaveEndedIt))
				winCX2 = false;
			if (winCY2 && (b1s[3-s][y][s] == false || b2s[3-s][y][s] != whoCouldHaveEndedIt))
				winCY2 = false;
			if (winCZ2 && (b1s[3-s][s][z] == false || b2s[3-s][s][z] != whoCouldHaveEndedIt))
				winCZ2 = false;
			

			if (winQ1 && (b1s[s][s][s] == false || b2s[s][s][s] != whoCouldHaveEndedIt))
				winQ1 = false;
			if (winQ2 && (b1s[s][s][3-s] == false || b2s[s][s][3-s] != whoCouldHaveEndedIt))
				winQ2 = false;
			if (winQ3 && (b1s[3-s][s][s] == false || b2s[3-s][s][s] != whoCouldHaveEndedIt))
				winQ3 = false;
			if (winQ4 && (b1s[3-s][s][3-s] == false || b2s[3-s][s][3-s] != whoCouldHaveEndedIt))
				winQ4 = false;
		}
		
		boolean hasEnded = winX || winY || winZ || winCX1 || winCX2 || winCY1 || winCY2 || winCZ1 || winCZ2 || winQ1 || winQ2 || winQ3 || winQ4;
		
		return hasEnded;
	}
	
	public static boolean isAir(boolean[][][] b1s, int[] coords)
	{
		return b1s[coords[0]][coords[1]][coords[2]] == false;
	}
	
	public static boolean isP1(boolean[][][] b2s, int[] coords)
	{
		return b2s[coords[0]][coords[1]][coords[2]] == false;
	}
	
	
	public static int getAmountOfLines(SogoGame g, int lineLength, Player player)
	{
		// ATTENTION: Doesnt work yet
		
		int linesAmount = 0;
		
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				int plAmount = 0;
				for (int z = 0; z < 4; z++)
				{
					if (g.board[x][y][z] == player)
					{
						plAmount++;
					}
					else if (g.board[x][y][z] == Player.NONE)
					{
						// Do nothing do to space
					}
					else	// In this case, its the other player
					{
						plAmount = -1;
						break;
					}
				}
				
				if (plAmount == lineLength)
				{
					linesAmount++;
				}
			}
		}
		
		return linesAmount;
	}
	
}
