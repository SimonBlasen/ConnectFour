package ki.sapph;

import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.SogoPlayer;

public class MrIdiot implements SogoPlayer {

	@Override
	public void initialize(Player p) {
		
	}

	@Override
	public void generateNextMove(SogoGameConsole c) {
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();

		for (int i = 0; i < moves.size(); i++)
		{
			SogoGame newGame = g.performMove(moves.get(i));
			
			List<SogoMove> newMoves = newGame.generateValidMoves();
			boolean canOtherWin = false;
			for (int j = 0; j < newMoves.size(); j++)
			{
				SogoGame finalGame = newGame.performMove(newMoves.get(j));
				
				if (finalGame.ends())
				{
					canOtherWin = true;
					break;
				}
			}
			
			if (canOtherWin == false)
			{
				c.updateMove(moves.get(i));
				break;
			}
		}
		
		
		//c.updateMove(moves.get(0));
	}

}
