package util;

import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;

public class GameAnalyzer {

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
