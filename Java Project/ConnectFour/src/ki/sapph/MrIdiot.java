package ki.sapph;

import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.SogoPlayer;

public class MrIdiot implements SogoPlayer {

	private Player p;
	
	@Override
	public void initialize(Player p) {
		this.p = p;
	}

	@Override
	public void generateNextMove(SogoGameConsole c) {
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();

		c.updateMove(moves.get(0));
		
		double value = 0.0;
		
		for (int i = 0; i < moves.size(); i++)
		{
			double newValue = depthSearch(g, 5, moves.get(i), value, true);
			if (newValue > value)
			{
				value = newValue;

				System.out.println("Updated: " + value);
				c.updateMove(moves.get(i));
			}
		}
		
		System.out.println("Finished calculation. Value: " + value);
	}
	
	private double depthSearch(SogoGame game, int depth, SogoMove move, double currentValue, boolean isMax)
	{
		SogoGame newGame = game.performMove(move);
		
		if (depth <= 0 || newGame.ends())
		{
			return evaluateGame(newGame);
		}
		else
		{
			final List<SogoMove> moves = newGame.generateValidMoves();
			
			currentValue = depthSearch(newGame, depth - 1, moves.get(0), currentValue, !isMax);
			
			for (int i = 0; i < moves.size(); i++)
			{
				double valueThere = depthSearch(newGame, depth - 1, moves.get(i), currentValue, !isMax);
				
				if (valueThere > currentValue == isMax)
				{
					currentValue = valueThere;
				}
			}
			
			return currentValue;
		}
	}
	
	
	private double evaluateGame(SogoGame game)
	{
		double val = MrNovice.evaluateGame(game);
		//System.out.println("Evaluated: " + val);
		
		return (game.getCurrentPlayer() != p ? -val : val);
	}

}
