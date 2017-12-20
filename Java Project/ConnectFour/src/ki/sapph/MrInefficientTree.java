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
	private SogoGameConsole c;
	
	@Override
	public void initialize(Player p) {
		this.p = p;
	}

	@Override
	public void generateNextMove(SogoGameConsole c)
	{
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		

		c.updateMove(moves.get(0));
		
		int tookIndex = 0;
		
		double maxYet = -10000.0;
		for (int depth = 4; depth < 20; depth++)
		{
			MrIdiotGamestate root = new MrIdiotGamestate(g, true, evaluateGame(g));
			
			for (int i = 0; i < moves.size(); i++)
			{
				SogoGame newG = g.performMove(moves.get(i));
				MrIdiotGamestate child = new MrIdiotGamestate(newG, false, evaluateGame(newG));
				child.setParent(root);
				
				evaluateNode(child, depth);
				
				System.out.println("[" + i + "] " + child.getValue());
				
				if (child.getValue() > maxYet)
				{
					maxYet = child.getValue();
					c.updateMove(moves.get(i));
					tookIndex = i;
				}
			}
			
			root = null;

			if (c.getTimeLeft() <= 0)
			{
				break;
			}
			System.out.println("Depth [" + depth + "] done");
		}
		
		System.out.println("Took: " + tookIndex);
	}
	
	
	private void evaluateNode(MrIdiotGamestate node, int depth)
	{
		if (c.getTimeLeft() <= 0)
		{
			return;
		}
		if (depth <= 0 || node.game.ends())
		{
			node.setValue(evaluateGame(node.game));
		}
		else
		{
			List<SogoMove> moves = node.game.generateValidMoves();
			for (int i = 0; i < moves.size(); i++)
			{
				SogoGame newG = node.game.performMove(moves.get(i));
				MrIdiotGamestate child = new MrIdiotGamestate(newG, !node.isMax);
				child.setParent(node);
					
				evaluateNode(child, depth - 1);
				node.updateValue(child.getValue());
					
				boolean didNodeChange = node.updateValue(child.getValue());
				if (didNodeChange && node.getParent().doesHelp(node.getValue()) == false)
				{
					// You can prun this path
					// The reason is, that 'node' found a better path in this child here. But it didnt help the parent, so the parent wont take any of the up comming children
					
					return;
				}
			}
			
			//node.setValue(calcMinMax(node));
		}
	}
	
	
	private double calcMinMax(MrIdiotGamestate node)
	{
		double curMax = (node.getChildren().size() > 0 ? node.getChildren().get(0).getValue() : 0.0);
		for (int i = 0; i < node.getChildren().size(); i++)
		{
			if (node.isValueSet() && node.getChildren().get(i).getValue() > curMax == node.isMax)
			{
				curMax = node.getChildren().get(i).getValue();
			}
		}
		
		return curMax;
	}
	
	
	private double evaluateGame(SogoGame game)
	{
		double val = MrNovice.evaluateGame(game);
		
		return (game.getCurrentPlayer() != p ? -val : val);
	}
}
