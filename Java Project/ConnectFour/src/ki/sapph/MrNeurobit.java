package ki.sapph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.SogoPlayer;
import heuristic.Net;
import util.GameAnalyzer;

public class MrNeurobit implements SogoPlayer {

	private Player p;
	private SogoGameConsole c;
	
	private static final double win_p = 1000.0 / 1000.0;
	private static final double triple_p = 20.0 / 1000.0;
	private static final double double_p = 2.0 / 1000.0;
	private static final double single_p = 1.0 / 1000.0;
	
	private static final double sapphassassiinoInfinity = 9999999.0;
	
	
	private int takeIndex = -1;
	
	private Net net;
	
	@Override
	public void initialize(Player p) {
		this.p = p;
		
		net = new Net("D:/Dokumente/connectfour_float.net");
	}

	
	
	@Override
	public void generateNextMove(SogoGameConsole c)
	{
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		
		double[] currentValues = new double[16];
		double[] nextValues = new double[16];
		
		c.updateMove(moves.get((new Random(0)).nextInt(moves.size())));
		
		
				
		for (int depth = 1; depth <= 20; depth += 1)
		{
			long bp1 = GameAnalyzer.getBP1FromGame(g);
			long bp2 = GameAnalyzer.getBP2FromGame(g);
			
			double val = evaluateNode(bp1, bp2, true, -300000.0, 300000.0, depth, true, 0, depth <= 2);
			
			if (c.getTimeLeft() <= 0)
			{
				break;
			}
			
			c.updateMove(new SogoMove(takeIndex % 4, takeIndex / 4));
					
			System.out.println("Depth [" + depth + "] done");
			System.out.println("Took value: " + takeIndex);
					
		}
	}
	
	private double evaluateNode(long bp1, long bp2, boolean isMax, double alpha, double beta, int depth, boolean firstNode, int moveIndex, boolean beginner)
	{
		// Execute move at moveIndex
		
		long placedField = 0x0L;
		
		if (firstNode == false)
		{
			for (int z = 0; z < 4; z++)
			{
				placedField = (0x1L << (moveIndex + z * 16));
				
				if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
				{
					if (!isMax)
					{
						bp1 = bp1 | placedField;
					}
					else
					{
						bp2 = bp2 | placedField;
					}
					break;
				}
			}
		}
		
		

		double bestValue = isMax ? -20000000.0 : 20000000.0;
		if (c.getTimeLeft() <= 0)
		{
			return bestValue;
		}
		
		

		if (depth <= 0 || GameAnalyzer.hasGameEnded(isMax ? bp2 : bp1))
		{
			double val = evaluateGame(bp1, bp2);
			return val;
		}
		else
		{
			for (int i = 0; i < 16; i++)
			{
				long dropPosition = (0x1L << (48 + i));
				
				if ((bp1 & dropPosition) == 0x0L && (bp2 & dropPosition) == 0x0L && c.getTimeLeft() > 0 /*&& forbidden[i] == false*/)
				{
					double childValue = evaluateNode(bp1, bp2, !isMax, alpha, beta, depth - 1, false, i, beginner);
					
					if (firstNode)
					{
						System.out.println("Move [" + i + "] is " + childValue);
					}
					
					if (childValue > bestValue == isMax)
					{
						if (firstNode && childValue < 2000.0)
						{
							takeIndex = i;
							//bestIndices.clear();
							//bestIndices.add(i);
						}
						bestValue = childValue;
					}
					
					if (isMax == false && childValue < beta)
					{
						beta = childValue;
					}
					else if (isMax && childValue > alpha)
					{
						alpha = childValue;
					}
					
					if (alpha >= beta && firstNode == false && beginner == false)
					{
						// Pruning     beta      alpha
						return isMax ? sapphassassiinoInfinity : -sapphassassiinoInfinity;
					}
				}
			}
			
			return bestValue;
		}
	}
	
	
	private int[] convertedGame = new int[64];
	
	private int[] gameToInputs(long bp1, long bp2)
	{
		long t1 = bp1;
		long t2 = bp2;
		
		for (int i = 0; i < convertedGame.length; i++)
		{
			if ((t1 & 0x1L) == 0x1L)
			{
				convertedGame[i] = 1;
			}
			else if ((t2 & 0x1L) == 0x1L)
			{
				convertedGame[i] = -1;
			}
			else
			{
				convertedGame[i] = 0;
			}
			
			t1 = t1 >> 1;
			t2 = t2 >> 1;
		}
		
		return convertedGame;
	}
	
	
	public double evaluateGame(long bp1, long bp2)
	{
		int[] inputs = gameToInputs(bp1, bp2);
		
		float res = Net.run(net, inputs);
		
		return res;
		
		/*double res = 0.0;
		
		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			int self = 0;
			int other = 0;
			
			long p1Here = bp1 & GameAnalyzer.longLines[i];
			long p2Here = bp2 & GameAnalyzer.longLines[i];
			
			while (p1Here != 0x0L)
			{
				p1Here &= (p1Here - 1);
				self++;
			}
			while (p2Here != 0x0L)
			{
				p2Here &= (p2Here - 1);
				other++;
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
		
		return res;*/
	}
	
	
	
	
	
}
