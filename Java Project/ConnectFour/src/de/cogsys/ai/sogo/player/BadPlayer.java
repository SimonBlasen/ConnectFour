package de.cogsys.ai.sogo.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.Sogo;
import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import util.GameAnalyzer;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;

public class BadPlayer implements SogoPlayer {
	
	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	private static int DEPTH = 5;
	
	private boolean debug_messages = false;
	
	private int mDepth = 0;
	private Random mRnd;
	private SogoGameConsole c;
	
	public BadPlayer() {
		this(DEPTH);
	}
	
	public BadPlayer(int depth) {
		this(depth, System.currentTimeMillis());
	}
	
	public BadPlayer(int depth, final long seed) {
		mRnd  = new Random(seed);
		mDepth = depth;
	}

	@Override
	public void initialize(Player p) {
		// TODO Auto-generated method stub
		
	}
	
	private int stonesAmountRound = 0;

	private SogoMove takeMove;
	
	@Override
	public void generateNextMove(SogoGameConsole c) {
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		final List<SogoMove> bestmoves = new ArrayList<SogoMove>();

		
		long bp1 = GameAnalyzer.getBP1FromGame(g);
		long bp2 = GameAnalyzer.getBP2FromGame(g);

		stonesAmountRound = countStones(bp1, bp2);

		
		
		takeMove = moves.get(0);
		
		for (int depth = 4; depth < 30; depth++)
		{
			double maxscore = Double.NEGATIVE_INFINITY;
			
			
			double score = max_value_toplevel(moves, bp1, bp2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);
			
			
			/*for (int i = 0; i < 16; i++)
			{
				for (SogoMove m : moves) {

					if (m.i == i % 4 && m.j == i / 4)
					{
						long placedField = 0x0L;



						long alteredBp1 = bp1;

						for (int z = 0; z < 4; z++)
						{
							placedField = (0x1L << (i + z * 16));

							if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
							{
								alteredBp1 = bp1 | placedField;
								break;
							}
						}


						final double score = min_value(alteredBp1, bp2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);


						if (debug_messages)
							System.out.println(m.i + "," + m.j + " = " + score);

						if (score > maxscore) {
							bestmoves.clear();
							bestmoves.add(m);
							maxscore = score;
						} else if (score == maxscore) {
							bestmoves.add(m);
						}
					}
				}
			}*/
			
			if (c.getTimeLeft() <= 0)
			{
				break;
			}
			else
			{
				/*double bestNormalHeurVal = Double.NEGATIVE_INFINITY;
				
				for (int j = 0; j < bestmoves.size(); j++)
				{
					long placedField = 0x0L;



					long alteredBp1 = bp1;

					for (int z = 0; z < 4; z++)
					{
						placedField = (0x1L << (j + z * 16));

						if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
						{
							alteredBp1 = bp1 | placedField;
							break;
						}
					}

					double result = evaluateGame(alteredBp1, bp2);
					
					if (result > bestNormalHeurVal)
					{
						c.updateMove(bestmoves.get(j));
						bestNormalHeurVal = result;
						System.out.println("Depth " + depth + " done, took: [" + bestmoves.get(j).i + "," + bestmoves.get(j).j + "]");
					}
				}*/

				c.updateMove(takeMove);
				//c.updateMove(bestmoves.get((new Random()).nextInt(bestmoves.size())));
				if (debug_messages && depth >= 6)
					System.out.println("Depth " + depth + " done, took: [" + takeMove.i + "," + takeMove.j + "]" + " Did I begin? " + ((stonesAmountRound % 2) == 0));
				
			}
		}



		
		
		SogoMove bestMove = null;
		double bestMoveValue = Double.NEGATIVE_INFINITY;

	        

	}
	
	public double max_value_toplevel(List<SogoMove> moves, long bp1, long bp2, double alpha, double beta, int remainingDepth)
	{
		double v = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < 16; i++)
		{
			for (SogoMove m : moves)
			{

				if (m.i == i % 4 && m.j == i / 4)
				{
					long dropPosition = (0x1L << (48 + i));

					long alteredBp1 = bp2;
					if ((bp1 & dropPosition) == 0x0L && (bp2 & dropPosition) == 0x0L && c.getTimeLeft() > 0)
					{
						long placedField = 0x0L;

						for (int z = 0; z < 4; z++)
						{
							placedField = (0x1L << (i + z * 16));

							if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
							{
								alteredBp1 = bp1 | placedField;
								break;
							}
						}
						
						
						// Check for killer boards
						
						boolean isKiller = GameAnalyzer.killerMove(alteredBp1, bp2);
						
						double value = 0.0;
						
						value = min_value(alteredBp1, bp2, alpha, beta, (remainingDepth-1), bp1);

						if (isKiller && value > -900.0)
						{
							value = 977.0;
							System.out.println("Got a killer");
						}
						else
						{
						}
						
						

						
						if (debug_messages)
							System.out.println(m.i + "," + m.j + " = " + value);

						if (value > v && value > -999.9)
						{
							takeMove = m;
							v = value;
						}

						//v = Math.max(v, value);
						if (v >= beta) {
							//System.out.println("pruning");
							//return v;
						}
						else {
							alpha = Math.max(alpha, v);
						}
					}
				}
			}
		}
		return v;
	}
	

	public double max_value(long bp1, long bp2, double alpha, double beta, int remainingDepth, long oldBp1)
	{
		if ((remainingDepth <= 0) || GameAnalyzer.hasGameEnded(bp2)) {
			
			return evaluateOddEven(bp1, bp2, oldBp1);
			//return evaluateMilton(bp1, bp2, true);
		}
		

		double v = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < 16; i++)
		{
			long dropPosition = (0x1L << (48 + i));

			long alteredBp1 = bp2;
			if ((bp1 & dropPosition) == 0x0L && (bp2 & dropPosition) == 0x0L && c.getTimeLeft() > 0)
			{
				long placedField = 0x0L;

				for (int z = 0; z < 4; z++)
				{
					placedField = (0x1L << (i + z * 16));

					if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
					{
						alteredBp1 = bp1 | placedField;
						break;
					}
				}

				final double value = min_value(alteredBp1, bp2, alpha, beta, (remainingDepth-1), bp1);
				v = Math.max(v, value);
				if (v >= beta) {
					//System.out.println("pruning");
					return v;
				}
				else {
					alpha = Math.max(alpha, v);
				}
			}
		}
		return v;

	}

	public double min_value(long bp1, long bp2, double alpha, double beta, int remainingDepth, long oldBp1)
	{
		if ((remainingDepth <= 0) || GameAnalyzer.hasGameEnded(bp1))
		{
			return evaluateOddEven(bp1, bp2, oldBp1);
			//return evaluateMilton(bp1, bp2, false);
	    }

	    double v = Double.POSITIVE_INFINITY;

	    for (int i = 0; i < 16; i++)
	    {
	    	long dropPosition = (0x1L << (48 + i));

	    	long alteredBp1 = bp2;
	    	if ((bp1 & dropPosition) == 0x0L && (bp2 & dropPosition) == 0x0L && c.getTimeLeft() > 0)
	    	{
	    		long placedField = 0x0L;

	    		for (int z = 0; z < 4; z++)
	    		{
	    			placedField = (0x1L << (i + z * 16));

	    			if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
	    			{
	    				alteredBp1 = bp2 | placedField;
	    				break;
	    			}
	    		}


	    		if ((new SogoMove(i % 4, i / 4)).i < 0 || (new SogoMove(i % 4, i / 4)).i >= 4 || (new SogoMove(i % 4, i / 4)).j < 0 || (new SogoMove(i % 4, i / 4)).j >= 4)
	    		{
	    			System.out.println("ARSCH");
	    		}

	    		final double value = max_value(bp1, alteredBp1, alpha, beta, (remainingDepth-1), bp1);
	    		v = Math.min(v, value);
	    		if (v <= alpha) {
	    			//System.out.println("pruning");
	    			return v;
	    		}
	    		else {
	    			beta = Math.min(beta, v);
	    		}
	    	}
	    }


	    return v;
	}
	
	public static double evaluateGame(final SogoGame g) {
		List<Player[]> lines = g.getLines();
		double res = 0;
		for (Player[] l : lines) {
			int self = SogoGame.countLine(l, g.getCurrentPlayer());
			int other = SogoGame.countLine(l, g.getOtherPlayer());
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
	
	
	public static double evaluateGame(long bp1, long bp2)
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
	
	
	public int completelyHisLines(long bpHis, long bpOther)
	{
		int own = 0;
		
		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			if (((bpHis & GameAnalyzer.longLines[i]) != 0x0L) && ((bpOther & GameAnalyzer.longLines[i]) == 0x0L))
			{
				own++;
			}
		}
		
		return own;
	}
	
	
	public int countUselessLines(long bpHis, long bpOther)
	{
		int own = 0;
		
		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			if ((((bpHis & GameAnalyzer.longLines[i]) != 0x0L) && ((bpOther & GameAnalyzer.longLines[i]) != 0x0L)) || (((bpHis & GameAnalyzer.longLines[i]) == 0x0L) && ((bpOther & GameAnalyzer.longLines[i]) == 0x0L)))
			{
				own++;
			}
		}
		
		return own;
	}
	
	

	
	private boolean[] threatPoses = new boolean[16];
	private boolean[] threatPoses2 = new boolean[16];
	
	/**
	 * 1 = p1 threat
	 * 2 = p2 threat
	 * 5 = p1 2-er threat
	 * 6 = p2 2-er threat
	 * 3 = stone
	 * 0 = air
	 * 
	 * 
	 */
	private byte[][] threats = new byte[16][4];
	
	
	
	public double evaluateOddEven(long bp1, long bp2, long oldBp1)
	{
		boolean isEnemyKiller = GameAnalyzer.killerMove(bp2, oldBp1);
		
		if (isEnemyKiller)
		{
			return -977.0;
		}

		//else -->
		
		boolean didIBegin = (stonesAmountRound % 2) == 0;
		int p1ThreatsOdd = 0;
		int p1ThreatsEven = 0;
		int p2ThreatsOdd = 0;
		int p2ThreatsEven = 0;

		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			long hereP1 = (bp1 & GameAnalyzer.longLines[i]) ^ GameAnalyzer.longLines[i];
			long hereP2 = (bp2 & GameAnalyzer.longLines[i]) ^ GameAnalyzer.longLines[i];
			
			if (hereP1 == 0x0L)
			{
				return win_p;
			}
			else if (hereP2 == 0x0L)
			{
				return -win_p;
			}
			
			int p1Amounts = 0;
			long p1Counter = hereP1;
			while (p1Counter != 0x0L)
			{
				p1Counter &= (p1Counter - 1);
				p1Amounts++;
			}
			int p2Amounts = 0;
			long p2Counter = hereP2;
			while (p2Counter != 0x0L)
			{
				p2Counter &= (p2Counter - 1);
				p2Amounts++;
			}
			
			
			
			if (p1Amounts == 1 && p2Amounts == 4)
			{
				if (hereP1 > 0xFFFFL)
				{
					long oneBelowP1 = (hereP1 >> 16);
					if (((oneBelowP1 & bp1) == 0x0L) && ((oneBelowP1 & bp2) == 0x0L))
					{
						// Floor number 2
						if (hereP1 <= 0xFFFFFFFFL)
						{
							p1ThreatsOdd++;
						}
						// Floor number 3
						else if (hereP1 <= 0xFFFFFFFFFFFFL)
						{
							p1ThreatsEven++;
						}
						// Floor number 4
						else if (hereP1 <= 0xFFFFFFFFFFFFFFFFL)
						{
							p1ThreatsOdd++;
						}
					}
				}
			}
			else if (p2Amounts == 1 && p1Amounts == 4)
			{
				if (hereP2 > 0xFFFFL)
				{
					long oneBelowP2 = (hereP2 >> 16);
					if (((oneBelowP2 & bp1) == 0x0L) && ((oneBelowP2 & bp2) == 0x0L))
					{
						// Floor number 2
						if (hereP2 <= 0xFFFFFFFFL)
						{
							p2ThreatsOdd++;
						}
						// Floor number 3
						else if (hereP2 <= 0xFFFFFFFFFFFFL)
						{
							p2ThreatsEven++;
						}
						// Floor number 4
						else if (hereP2 <= 0xFFFFFFFFFFFFFFFFL)
						{
							p2ThreatsOdd++;
						}
					}
				}
			}
		}
		
		
		/*
		 * Floor
		 * 4   	  Odd
		 * 3   	  Even
		 * 2   	  Odd
		 * 1      Even
		 * 
		 */
		
		int difference = 0;
		if (didIBegin)
		{
			difference = p1ThreatsEven - p2ThreatsOdd;			
		}
		else
		{
			difference = p1ThreatsOdd - p2ThreatsEven;
		}
		
		return (difference * 100.0) + evaluateGame(bp1, bp2) * 0.06;
	}
	
	
	public static double evaluateOddEven(long bp1, long bp2, boolean didIBegin)
	{
		int p1ThreatsOdd = 0;
		int p1ThreatsEven = 0;
		int p2ThreatsOdd = 0;
		int p2ThreatsEven = 0;

		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			long hereP1 = (bp1 & GameAnalyzer.longLines[i]) ^ GameAnalyzer.longLines[i];
			long hereP2 = (bp2 & GameAnalyzer.longLines[i]) ^ GameAnalyzer.longLines[i];
			
			if (hereP1 == 0x0L)
			{
				return win_p;
			}
			else if (hereP2 == 0x0L)
			{
				return -win_p;
			}
			
			int p1Amounts = 0;
			long p1Counter = hereP1;
			while (p1Counter != 0x0L)
			{
				p1Counter &= (p1Counter - 1);
				p1Amounts++;
			}
			int p2Amounts = 0;
			long p2Counter = hereP2;
			while (p2Counter != 0x0L)
			{
				p2Counter &= (p2Counter - 1);
				p2Amounts++;
			}
			
			
			
			if (p1Amounts == 1 && p2Amounts == 4)
			{
				if (hereP1 > 0xFFFFL)
				{
					long oneBelowP1 = (hereP1 >> 16);
					if (((oneBelowP1 & bp1) == 0x0L) && ((oneBelowP1 & bp2) == 0x0L))
					{
						// Floor number 2
						if (hereP1 <= 0xFFFFFFFFL)
						{
							p1ThreatsOdd++;
						}
						// Floor number 3
						else if (hereP1 <= 0xFFFFFFFFFFFFL)
						{
							p1ThreatsEven++;
						}
						// Floor number 4
						else if (hereP1 <= 0xFFFFFFFFFFFFFFFFL)
						{
							p1ThreatsOdd++;
						}
					}
				}
			}
			else if (p2Amounts == 1 && p1Amounts == 4)
			{
				if (hereP2 > 0xFFFFL)
				{
					long oneBelowP2 = (hereP2 >> 16);
					if (((oneBelowP2 & bp1) == 0x0L) && ((oneBelowP2 & bp2) == 0x0L))
					{
						// Floor number 2
						if (hereP2 <= 0xFFFFFFFFL)
						{
							p2ThreatsOdd++;
						}
						// Floor number 3
						else if (hereP2 <= 0xFFFFFFFFFFFFL)
						{
							p2ThreatsEven++;
						}
						// Floor number 4
						else if (hereP2 <= 0xFFFFFFFFFFFFFFFFL)
						{
							p2ThreatsOdd++;
						}
					}
				}
			}
		}
		
		
		/*
		 * Floor
		 * 4   	  Odd
		 * 3   	  Even
		 * 2   	  Odd
		 * 1      Even
		 * 
		 */
		
		int difference = 0;
		if (didIBegin)
		{
			difference = p1ThreatsEven - p2ThreatsOdd;			
		}
		else
		{
			difference = p1ThreatsOdd - p2ThreatsEven;
		}
		
		return (difference * 100.0) + evaluateGame(bp1, bp2);
	}
	
	
	
	
	
	
	public double evaluateMilton(long bp1, long bp2, boolean turnP1)
	{
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i] /*|| threatPoses2[i]*/)
			{
				for (int j = 0; j < threats[i].length; j++)
				{
					threats[i][j] = 0;
				}
				threatPoses[i] = false;
				//threatPoses2[i] = false;
			}
		}
		
		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			long p1Here = bp1 & GameAnalyzer.longLines[i];
			long p2Here = bp2 & GameAnalyzer.longLines[i];

			int self = 0;
			int other = 0;
			
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

			long threatPos2_0 = 0x0L;
			long threatPos2_1 = 0x0L;
			
			long threatPos = 0x0L;
			boolean pThreat = false;
			
			if (self == 0 && other == 3)
			{
				threatPos = bp2 & GameAnalyzer.longLines[i];
				threatPos = threatPos ^ GameAnalyzer.longLines[i];
			}
			else if (self == 3 && other == 0)
			{
				threatPos = bp1 & GameAnalyzer.longLines[i];
				threatPos = threatPos ^ GameAnalyzer.longLines[i];
				pThreat = true;
			}
			else if (self == 4)
			{
				return win_p;
			}
			else if (other == 4)
			{
				return -win_p;
			}
			/*else if (self == 2 && other == 0)
			{
				threatPos2_0 = (bp1 & GameAnalyzer.longLines[i]) & ((bp1 & GameAnalyzer.longLines[i]) - 0x1L);
				threatPos2_1 = (bp1 & GameAnalyzer.longLines[i]) ^ threatPos2_0;
				pThreat = true;
			}
			else if (self == 0 && other == 2)
			{
				threatPos2_0 = (bp2 & GameAnalyzer.longLines[i]) & ((bp2 & GameAnalyzer.longLines[i]) - 0x1L);
				threatPos2_1 = (bp2 & GameAnalyzer.longLines[i]) ^ threatPos2_0;
			}*/
			
			
			if (threatPos != 0x0L)
			{
				int floor = 0;
				while (threatPos > 0xFFFFL)
				{
					threatPos = threatPos >> 16;
					floor++;
				}
				
				for (int j = 0; j < GameAnalyzer.bottomLines.length; j++)
				{
					if (threatPos == GameAnalyzer.bottomLines[j])
					{
						if (threatPoses[j] == false)
						{
							for (int k = 0; k < floor; k++)
							{
								long pBottom = (threatPos << (k * 16)) & (bp1 | bp2);
								if (pBottom == 0x0L)
								{
									threats[j][k] = 0;
									break;
								}
								else
								{
									threats[j][k] = 3;
								}
							}
						}
						
						threatPoses[j] = true;
						threats[j][floor] = pThreat ? (byte)1 : (byte)2;
						break;
					}
				}
			}
			/*else if (threatPos2_0 != 0x0L)
			{
				int floor0 = 0;
				while (threatPos2_0 > 0xFFFFL)
				{
					threatPos2_0 = threatPos2_0 >> 16;
					floor0++;
				}
				int floor1 = 0;
				while (threatPos2_1 > 0xFFFFL)
				{
					threatPos2_1 = threatPos2_1 >> 16;
					floor1++;
				}
				
				
				for (int j = 0; j < GameAnalyzer.bottomLines.length; j++)
				{
					if (threatPos2_0 == GameAnalyzer.bottomLines[j])
					{
						if (threatPoses2[j] == false)
						{
							for (int k = 0; k < floor0; k++)
							{
								long pBottom = (threatPos2_0 << (k * 16)) & (bp1 | bp2);
								if (pBottom == 0x0L)
								{
									threats[j][k] = 0;
									break;
								}
								else
								{
									threats[j][k] = 3;
								}
							}
						}
						
						threatPoses2[j] = true;
						threats[j][floor0] = pThreat ? (byte)5 : (byte)6;
						
					}
					
					
					if (threatPos2_1 == GameAnalyzer.bottomLines[j])
					{
						if (threatPoses2[j] == false)
						{
							for (int k = 0; k < floor1; k++)
							{
								long pBottom = (threatPos2_1 << (k * 16)) & (bp1 | bp2);
								if (pBottom == 0x0L)
								{
									threats[j][k] = 0;
									break;
								}
								else
								{
									threats[j][k] = 3;
								}
							}
						}
						
						threatPoses2[j] = true;
						threats[j][floor1] = pThreat ? (byte)5 : (byte)6;
						
					}
				}
				
			}*/
		}
		
	
		/*byte result = recThreatning(true);
		if (result == 1)
		{
			return win_p * 0.9;
		}
		else if (result == -1)
		{
			return -win_p * 0.9;
		}
		else
		{
			return evaluateGame(bp1, bp2);
			//return 0.0;
		}*/
		
		//byte result = recThreatning(turnP1);
		
		double resultP1 = 0;
		double resultP2 = 0;
		double result = 0;
		double resultP1_2er = 0;
		double resultP2_2er = 0;
		
		double amountsP1 = 0;
		double amountsP2 = 0;
		
//		double[] floors = new double[] {3, 2.5, 2.5, 1.6};
		double[] floors = new double[] {1.0, 300.0, 90.0, 10.0};
		//double[] floors = new double[] {Sogo.weight1, Sogo.weight2, Sogo.weight3, Sogo.weight4};
		
		
		boolean didP1Begin = (stonesAmountRound % 2) == 0;
		
		boolean useOddEven = true;
		
		
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				/*for (int j = 0; j < threats[i].length; j++)
				{
					if (threats[i][j] == 5 && (j == 0 || (threats[i][j - 1] != 2 && threats[i][j - 1] != 6)) && (turnP1 == true || (j >= 1 && (threats[i][j - 1] != 3))))
					{
						amountsP1++;
						resultP1 += floors[j];
					}
					else if (threats[i][j] == 6 && (j == 0 || (threats[i][j - 1] != 1 && threats[i][j - 1] != 5)) && (turnP1 == false || (j >= 1 && (threats[i][j - 1] != 3))))
					{
						amountsP2++;
						resultP2 += floors[j];
					}
				}*/
				
				for (int j = 0; j < threats[i].length; j++)
				{
					if (useOddEven)
					{
						if (threats[i][j] == 1)
						{
							if (((j % 2) == 0) == didP1Begin)
							{
								if (turnP1 && j == 0) 
								{
									return win_p;
								}
								else if (turnP1 == false && j == 0)
								{
									
								}
								else if (turnP1 == false && j > 0)
								{
									if (threats[i][j - 1] == 3)
									{
										
									}
									else if (threats[i][j - 1] == 0)
									{
										amountsP1++;
									}
									else if (threats[i][j - 1] == 2)
									{

									}
									else if (threats[i][j - 1] == 1)
									{
										amountsP1++;
									}
								}
								else if (turnP1 && j > 0)
								{
									amountsP1++;
								}
								
								
								
								/*if (j == 0 || (threats[i][j - 1] != 2))
								{
									amountsP1++;
								}*/
							}
						}
						else if (threats[i][j] == 2)
						{
							if (((j % 2) == 1) == didP1Begin)
							{
								if (turnP1 == false && j == 0) 
								{
									return -win_p;
								}
								else if (turnP1 && j == 0)
								{

								}
								else if (turnP1 && j > 0)
								{
									if (threats[i][j - 1] == 3)
									{

									}
									else if (threats[i][j - 1] == 0)
									{
										amountsP2++;
									}
									else if (threats[i][j - 1] == 1)
									{

									}
									else if (threats[i][j - 1] == 2)
									{
										amountsP2++;

									}
								}
								else if (turnP1 == false && j > 0)
								{
									amountsP2++;
								}
								
								/*if (j == 0 || (threats[i][j - 1] != 1))
								{
									amountsP2++;
								}*/
							}
						}
					}
					else
					{
						if (threats[i][j] == 1)
						{
							if (turnP1 && j == 0) 
							{
								return win_p;
							}
							else if (turnP1 == false && j == 0)
							{
								
							}
							else if (turnP1 == false && j > 0)
							{
								if (threats[i][j - 1] == 3)
								{
									
								}
								else if (threats[i][j - 1] == 0)
								{
									amountsP1++;
									resultP1 += floors[j];
								}
								else if (threats[i][j - 1] == 2)
								{

								}
								else if (threats[i][j - 1] == 1)
								{
									amountsP1++;
									resultP1 += floors[j];
								}
							}
							else if (turnP1 && j > 0)
							{
								amountsP1++;
								resultP1 += floors[j];
							}
						}

						else if (threats[i][j] == 2)
						{
							if (turnP1 == false && j == 0) 
							{
								return -win_p;
							}
							else if (turnP1 && j == 0)
							{

							}
							else if (turnP1 && j > 0)
							{
								if (threats[i][j - 1] == 3)
								{

								}
								else if (threats[i][j - 1] == 0)
								{
									amountsP2++;
									resultP2 += floors[j];
								}
								else if (threats[i][j - 1] == 1)
								{

								}
								else if (threats[i][j - 1] == 2)
								{
									amountsP2++;
									resultP2 += floors[j];

								}
							}
							else if (turnP1 == false && j > 0)
							{
								amountsP2++;
								resultP2 += floors[j];
							}
						}
					}
					
					/*if (threats[i][j] == 1 && (j == 0 || threats[i][j - 1] != 2) && (turnP1 == true || (j >= 1 && (threats[i][j - 1] != 3))))
					{
						amountsP1++;
						resultP1 += floors[j];
					}
					else if (threats[i][j] == 2 && (j == 0 || threats[i][j - 1] != 1) && (turnP1 == false || (j >= 1 && (threats[i][j - 1] != 3))))
					{
						amountsP2++;
						resultP2 += floors[j];
					}*/
				}
			}
		}
		
		//resultP1 *= (amountsP1);
		//resultP2 *= (amountsP2);
		
		
		result = resultP1 - resultP2;
		
		if (result <= -950 || result >= 905)
		{
		}
		
		
		
		
		//int stonesAmount = countStones(bp1, bp2);
		//int openLinesAmount = countOpenLines(bp1, bp2);
		
		
		if (stonesAmountRound <= 4)
		{
			return evaluateGame(bp1, bp2);
		}
		else
		{
			if (amountsP1 == 0 && amountsP2 == 0)
			{
				return evaluateGame(bp1, bp2);
			}
			else
			{
				//int p1Own = completelyHisLines(bp1, bp2);
				//int p2Own = completelyHisLines(bp2, bp1);
				
				//double toP1Advantage = (p1Own - p2Own);
				
				return (amountsP1 * 80.0 - amountsP2 * 80.0) + evaluateGame(bp1, bp2) * 0.06;
				//return (amountsP1 * 80.0 - amountsP2 * 60.0) + toP1Advantage;
			}
			
			
			/*double badness = -potentialFours;
			//double badness = (64.0 - stonesAmount) * 3.0;
			//double badness = openLinesAmount * 2.5;
			
			if (result == 1)
			{
				double milton = turnP1 ? win_p * 0.9 - badness : -win_p * 0.9 + badness;
				double heuristic = evaluateGame(bp1, bp2);
				return milton;
				//return turnP1 ? Math.max(milton, heuristic) : Math.max(milton, heuristic);
			}
			else if (result == -1)
			{
				double milton = turnP1 ? -win_p * 0.9 + badness : win_p * 0.9 - badness;
				double heuristic = evaluateGame(bp1, bp2);
				return milton;
				//return turnP1 ? Math.max(milton, heuristic) : Math.max(milton, heuristic);
				

			}
			else
			{
				return evaluateGame(bp1, bp2);
				//return 0.0;
			}*/
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	public int countStones(long bp1, long bp2) {
		int stonesAmout = 0;
		
		while (bp1 != 0x0L)
		{
			bp1 &= (bp1 - 1L);
			stonesAmout++;
			
		}
		while (bp2 != 0x0L)
		{
			bp2 &= (bp2 - 1L);
			stonesAmout++;
			
		}

		return stonesAmout;
	}
	
	
	
	
	
	
	
	
	
	

}
