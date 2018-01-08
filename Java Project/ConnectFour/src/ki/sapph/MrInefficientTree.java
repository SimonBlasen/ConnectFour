package ki.sapph;

import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.SogoPlayer;
import util.GameAnalyzer;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;

public class MrInefficientTree implements SogoPlayer {

	private Player p;
	private SogoGameConsole c;

	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	
	
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
		for (int depth = 5; depth <= 20; depth += 2)
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
					
				//boolean didNodeChange = node.updateValue(child.getValue());
				if (node.canPrun())
				{
					//System.out.println("Prun alpha");
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
		double val = evaluateGameG(game);
		
		return (game.getCurrentPlayer() != p ? -val : val);
	}
	
	public static double evaluateGameG(final SogoGame g) {
		
		List<Player[]> lines = g.getLines();
		double res = 0;
		for (Player[] l : lines) {
			int self = 0; /* GameAnalyzer.countFreeLine(l, g.getCurrentPlayer()); */
			int other = 0; /* GameAnalyzer.countFreeLine(l, g.getOtherPlayer()); */
			if (other == 0) {
				switch (self) {
				case 4:
					return win_p;
				case 3:
					res += triple_p;
					break;
				case 2:
					res += double_p;
					break;
				case 1:
					res += single_p;
					break;
				default:
					break;
				}
			}
			if (self == 0) {
				switch (other) {
				case 4:
					return -win_p;
				case 3:
					res -= triple_p;
					break;
				case 2:
					res -= double_p;
					break;
				case 1:
					res -= single_p;
					break;
				default:
					break;
				}
			}
		}
		return res;
	}
}
