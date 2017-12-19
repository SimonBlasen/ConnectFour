package ki.sapph;

import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.SogoPlayer;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;

public class MrInefficientTree implements SogoPlayer {

	private Player p;
	
	@Override
	public void initialize(Player p) {
		this.p = p;
	}

	@Override
	public void generateNextMove(SogoGameConsole c)
	{
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		
		MrIdiotGamestate root = new MrIdiotGamestate(g, true, evaluateGame(g));
		
		for (int i = 0; i < moves.size(); i++)
		{
			SogoGame newG = g.performMove(moves.get(i));
			MrIdiotGamestate child = new MrIdiotGamestate(newG, false, evaluateGame(newG));
			child.setParent(root);
			
			System.out.println("[" + i + "] " + child.value);
		}
		
		
		double maxYet = -10000.0;
		int maxIndex = -1;
		for (int i = 0; i < root.getChildren().size(); i++)
		{
			if (root.getChildren().get(i).value > maxYet)
			{
				maxYet = root.getChildren().get(i).value;
				maxIndex = i;
			}
		}
		
		c.updateMove(moves.get(maxIndex));
	}

	
	
	private double evaluateGame(SogoGame game)
	{
		double val = MrNovice.evaluateGame(game);
		//System.out.println("Evaluated: " + val);
		
		return (game.getCurrentPlayer() != p ? -val : val);
	}
}
