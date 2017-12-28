package ki.sapph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.player.SogoPlayer;
import util.GameAnalyzer;

public class MrBitwiseTree implements SogoPlayer {

	private Player p;
	private SogoGameConsole c;

	private boolean doneFirstMove = false;
	
	private static final double win_p = 1000.0 / 1000.0;
	private static final double triple_p = 20.0 / 1000.0;
	private static final double double_p = 2.0 / 1000.0;
	private static final double single_p = 1.0 / 1000.0;
	
	private static final double sapphassassiinoInfinity = 9999999.0;
	
	
	private List<Integer> bestIndices = new ArrayList<Integer>();
	private int takeIndex = -1;
	boolean[] forbidden = new boolean[16];
	private int safeWin = -1;
	
	public double[] calcMoves = new double[16];
	
	@Override
	public void initialize(Player p) {
		this.p = p;
	}
	
	private int counterP = 0;

	public int generateNextMoveMuchBetter(long p1, long p2)
	{
		c = new SogoGameConsole() {
			
			@Override
			public void updateMove(SogoMove move) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public long getTimeLeft() {
				// TODO Auto-generated method stub
				return 100000000L;
			}
			
			@Override
			public SogoGame getGame() {
				return new SogoGame();
			}
		};
		
		double[] currentValues = new double[16];
		double[] nextValues = new double[16];
		
		
		
		
		if (false && counterP <= 4)
		{
			c.updateMove(new SogoMove(counterP, 0));
			counterP++;
		}
		else
		{
			if (true || doneFirstMove)
			{
				int tookIndex = 0;
				
				double maxYet = -10000.0;
				
				
				for (int depth = 4; depth <= 4; depth += 1)
				{
					//boolean[][][] b1s = GameAnalyzer.getB1sFromGame(g);
					//boolean[][][] b2s = GameAnalyzer.getB2sFromGame(g);

					long bp1 = p1;
					long bp2 = p2;
					
					
					
					
					//System.out.println("P1: " + Long.toBinaryString(bp1));
					//System.out.println("P2: " + Long.toBinaryString(bp2));
					
					//double val = evaluateNode(b1s, b2s, true, -1000000.0, 1000000.0, depth, true, 0);
					
					double val = evaluateNode(bp1, bp2, true, -300000.0, 300000.0, depth, true, 0, depth <= 2);
					

					//int took = (new Random(0)).nextInt(bestIndices.size());

					if (true)
						
					{

						return takeIndex;
					}
					
					int forbiddenAmount = 0;
					for (int i = 0; i < forbidden.length; i++)
					{
						forbiddenAmount += forbidden[i] ? 1 : 0;
					}
					if (forbiddenAmount >= 15)
					{
						break;
					}
					
					
					System.out.println("Depth [" + depth + "] done");
					
					
					if (safeWin != -1)
					{
						System.out.println("---- Took a safe win ---- at " + safeWin);
						break;
					}
					else
					{
						System.out.println("Took value: " + takeIndex);
					}
				}
				
			}
			else
			{
				doneFirstMove = true;
			}
		}
		
		
		return 0;
		
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
		
		safeWin = -1;
		
		bestIndices.clear();
		for (int i = 0; i < forbidden.length; i++)
		{
			forbidden[i] = false;
		}
		
		
		if (false && counterP <= 4)
		{
			c.updateMove(new SogoMove(counterP, 0));
			counterP++;
		}
		else
		{
			if (true || doneFirstMove)
			{
				int tookIndex = 0;
				
				double maxYet = -10000.0;
				
				SogoMove nextMove = moves.get(0);
				
				for (int depth = 1; depth <= 20; depth += 1)
				{
					//boolean[][][] b1s = GameAnalyzer.getB1sFromGame(g);
					//boolean[][][] b2s = GameAnalyzer.getB2sFromGame(g);

					long bp1 = GameAnalyzer.getBP1FromGame(g);
					long bp2 = GameAnalyzer.getBP2FromGame(g);
					
					
					for (int x = 0; x < 4; x++)
					{
						for (int y = 0; y < 4; y++)
						{
							for (int z = 0; z < 4; z++)
							{
								long temp = 0x1L << (x + y * 4 + z * 16);
								//System.out.println(g.board[x][y][z] + " : " + (bp1 & temp) + " : " + (bp2 & temp));
							}
						}
					}
					
					
					
					
					//System.out.println("P1: " + Long.toBinaryString(bp1));
					//System.out.println("P2: " + Long.toBinaryString(bp2));
					
					//double val = evaluateNode(b1s, b2s, true, -1000000.0, 1000000.0, depth, true, 0);
					
					double val = evaluateNode(bp1, bp2, true, -300000.0, 300000.0, depth, true, 0, depth <= 2);
					

					if (c.getTimeLeft() <= 0)
					{
						break;
					}
					//int took = (new Random(0)).nextInt(bestIndices.size());
					
					if (safeWin != -1)
					{
						c.updateMove(new SogoMove(safeWin % 4, safeWin / 4));
					}
					else
					{
						c.updateMove(new SogoMove(takeIndex % 4, takeIndex / 4));
					}
					
					int forbiddenAmount = 0;
					for (int i = 0; i < forbidden.length; i++)
					{
						forbiddenAmount += forbidden[i] ? 1 : 0;
					}
					if (forbiddenAmount >= 15)
					{
						break;
					}
					
					
					System.out.println("Depth [" + depth + "] done");
					
					
					if (safeWin != -1)
					{
						System.out.println("---- Took a safe win ---- at " + safeWin);
						break;
					}
					else
					{
						System.out.println("Took value: " + takeIndex);
					}
				}
				
			}
			else
			{
				doneFirstMove = true;
			}
		}
		
		
		
		
		
		
	}
	
	private boolean showAll = false;
	
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

					if (firstNode && childValue == -1000.0 && beginner)
					{
						//forbidden[i] = true;
					}
					
					if (firstNode && childValue == 1000.0 && safeWin == -1)
					{
						//safeWin = i;
					}
					
					if (childValue >= 1000.0 && firstNode)
					{
						showAll = true;
					}
					
					if (firstNode)
					{
						calcMoves[i] = childValue;
						
						//if (depth >= 6 || showAll)
						//System.out.println("Move [" + i + "] is " + (childValue != 10000.0 && childValue != -10000.0 ? childValue : "pruned" + (forbidden[i] ? " IS FORBIDDEN" : "")));
						//System.out.println("Move [" + i + "] is in long: " + Long.toBinaryString(dropPosition));
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
					else if (childValue == bestValue)
					{
						if (firstNode)
						{
							//bestIndices.add(i);
						}
					}
					if (isMax == false && childValue < beta)
					{
						beta = childValue;
					}
					else if (isMax && childValue > alpha)
					{
						alpha = childValue;
					}
					
					//node.updateValue(child.getValue());
					
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
	
	private double evaluateNode(boolean[][][] bsp1, boolean[][][] bsp2, boolean isMax, double alpha, double beta, int depth, boolean firstNode, int moveIndex)
	{
		int x = moveIndex % 4;
		int y = moveIndex / 4;
		int z = 0;
		
		if (firstNode == false)
		{
			for (int i = 0; i < 4; i++)
			{
				if (bsp1[x][y][i] == false)
				{
					z = i;
					bsp1[x][y][i] = true;
					bsp2[x][y][i] = isMax;
					break;
				}
			}
		}
		
		double bestValue = isMax ? -10000.0 : 10000.0;
		
		
		if (c.getTimeLeft() <= 0)
		{
			if (firstNode == false)
			{
				bsp1[x][y][z] = false;
			}
			
			return -10000.0;
		}
		if (depth <= 0 || GameAnalyzer.hasGameEnded(bsp1, bsp2, x, y, z, isMax))
		{
			double val = evaluateGame(bsp1, bsp2);
			if (firstNode == false)
			{
				bsp1[x][y][z] = false;
			}
			return val;
		}
		else
		{
			for (int i = 0; i < 16; i++)
			{
				if (bsp1[i % 4][i / 4][3] == false && c.getTimeLeft() > 0)
				{
					double childValue = evaluateNode(bsp1, bsp2, !isMax, alpha, beta, depth - 1, false, i);
					
					if (firstNode)
					{
						if (depth >= 7)
							System.out.println("Move [" + i + "] is " + childValue);
					}
					
					if (childValue > bestValue == isMax)
					{
						if (firstNode)
						{
							bestIndices.clear();
							bestIndices.add(i);
						}
						bestValue = childValue;
					}
					else if (childValue == bestValue)
					{
						if (firstNode)
						{
							//bestIndices.add(i);
						}
					}
					if (isMax == false && childValue < beta)
					{
						beta = childValue;
					}
					else if (isMax && childValue > alpha)
					{
						alpha = childValue;
					}
					
					//node.updateValue(child.getValue());
					
					if (alpha >= beta && firstNode == false)
					{
						// Pruning
						
						if (firstNode == false)
						{
							bsp1[x][y][z] = false;
						}
						return isMax ? 10000.0 : -10000.0;
					}
				}
			}
			
			if (firstNode == false)
			{
				bsp1[x][y][z] = false;
			}
			return bestValue;
		}
	}
	
	public double evaluateGame(long bp1, long bp2)
	{
		double res = 0.0;
		
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
		
		return res;
	}
	
	
	private double evaluateGame(boolean[][][] b1s, boolean[][][] b2s)
	{
		double res = 0.0;
		
		for (int i = 0; i < GameAnalyzer.lines.length; i++)
		{
			int self = 0;
			int other = 0;
			
			for (int j = 0; j < 4; j++)
			{
				self += GameAnalyzer.isAir(b1s, GameAnalyzer.lines[i][j]) ? 0 : (GameAnalyzer.isP1(b2s, GameAnalyzer.lines[i][j]) ? 1 : 0);
				other += GameAnalyzer.isAir(b1s, GameAnalyzer.lines[i][j]) ? 0 : ((GameAnalyzer.isP1(b2s, GameAnalyzer.lines[i][j]) == false) ? 1 : 0);
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
