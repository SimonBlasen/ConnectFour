package ki.sapph;

import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.SogoPlayer;
import util.GameAnalyzer;

public class MrMoreefficientTree implements SogoPlayer {

	private Player p;
	private SogoGameConsole c;

	private boolean doneFirstMove = false;
	
	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	
	
	@Override
	public void initialize(Player p) {
		this.p = p;
	}
	

	public double[] calcMoves = new double[16];

	@Override
	public void generateNextMove(SogoGameConsole c)
	{
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		
		double[] currentValues = new double[16];
		double[] nextValues = new double[16];
		
		c.updateMove(moves.get((new Random(0)).nextInt(moves.size())));
		
		if (true || doneFirstMove)
		{
			int tookIndex = 0;
			
			double maxYet = -10000.0;
			
			SogoMove nextMove = moves.get(0);
			
			for (int depth = 4; depth <= 4; depth += 1)
			{
				MrMoreefficientGamestate root = new MrMoreefficientGamestate(true);
				root.rootGame = g;
				root.applyGame(g);
				
				evaluateNode(root, depth, true);
				

				int temptookIndex = 0;
				
				maxYet = -10000.0;
				for (int i = 0; i < 16; i++)
				{
					if (root.getChildren()[i] != null && depth >= 2)
					{
						calcMoves[i] = root.getChildren()[i].getValue();
						//System.out.println("[" + i + "] " + root.getChildren()[i].getValue() + " " + (root.getChildren()[i].isPruned ? "P" : ""));
					}
					if (root.getChildren()[i] != null && root.getChildren()[i].getValue() > maxYet && root.getChildren()[i].isPruned == false)
					{
						maxYet = root.getChildren()[i].getValue();
						nextMove = new SogoMove(i % 4, i / 4);
						temptookIndex = i;
					}
				}
				
				if (c.getTimeLeft() > 0 && maxYet > -999.0)
				{
					c.updateMove(nextMove);
					tookIndex = temptookIndex;
				}
				
				/*for (int i = 0; i < 16; i++)
				{
					if (root.canMoveAt(i))
					{
						MrMoreefficientGamestate child = new MrMoreefficientGamestate(false);
						child.setParent(root, i);
						
						evaluateNode(child, depth);

						if (c.getTimeLeft() <= 0)
						{
							break;
						}
						
						System.out.println("[" + i + "] " + child.getValue());
							
						currentValues[i] = child.getValue();
						
						//if (child.getValue() > maxYet)
						//{
						//	maxYet = child.getValue();
						//	c.updateMove(new SogoMove(i % 4, i / 4));
						//	tookIndex = i;
						//}
						
					}
					else
					{
						currentValues[i] = -100000.0;
					}
					/*
					
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
					}*/
				

				if (c.getTimeLeft() <= 0)
				{
					break;
				}
				//System.out.println("Depth [" + depth + "] done");
			}
			
			//System.out.println("Took: " + tookIndex);
		}
		else
		{
			doneFirstMove = true;
		}
		
		
	}
	
	private void evaluateNode(MrMoreefficientGamestate node, int depth, boolean firstNode)
	{

		if (c.getTimeLeft() <= 0)
		{
			return;
		}
		if (depth <= 0 || node.hasEnded())
		{
			node.setValue(evaluateGame(node));
		}
		else
		{
			for (int i = 0; i < 16; i++)
			{
				if (node.canMoveAt(i) && c.getTimeLeft() > 0)
				{
					MrMoreefficientGamestate child = new MrMoreefficientGamestate(!node.isMax);
					child.setParent(node, i);
					
					evaluateNode(child, depth - 1, false);
					node.updateValue(child.getValue());
					
					if (false && node.canPrun() && firstNode == false)
					{
						node.isPruned = true;
						return;
					}
				}
			}
		}
	}
	
	
	
	private double evaluateGame(MrMoreefficientGamestate game)
	{
		double val = evaluateGameG(game);
		
		/*SogoGame g = new SogoGame();
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					if (game.fieldAir(x, y, z) == true)
					{
						g.board[x][y][z] = Player.NONE;
					}
					else
					{
						g.board[x][y][z] = game.fieldP(x, y, z, false) ? Player.P1 : Player.P2;
					}
				}
			}
		}
		
		double val = MrNovice.evaluateGame(g);*/
		
		return val;
	}
	
	public static double evaluateGameG(MrMoreefficientGamestate g)
	{
		double res = 0.0;
		
		for (int i = 0; i < GameAnalyzer.lines.length; i++)
		{
			int self = 0;
			int other = 0;
			
			for (int j = 0; j < 4; j++)
			{
				self += g.fieldAir(GameAnalyzer.lines[i][j]) ? 0 : (g.fieldP(GameAnalyzer.lines[i][j], false) ? 1 : 0);
				other += g.fieldAir(GameAnalyzer.lines[i][j]) ? 0 : (g.fieldP(GameAnalyzer.lines[i][j], true) ? 1 : 0);
			}
			
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
