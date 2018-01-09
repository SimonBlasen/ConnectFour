package de.cogsys.ai.sogo.player;

import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import util.GameAnalyzer;
import util.OpeningDatabase;
import util.OpeningStrategy;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;

public class BadPlayer implements SogoPlayer {
	
	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	
	private boolean debug_messages = true;
	
	private SogoGameConsole c;
	
	private Player selfPlayer;
	
	public BadPlayer() {
		
	}

	@Override
	public void initialize(Player p)
	{
		selfPlayer = p;
		OpeningDatabase.setStrategy(OpeningStrategy.DIAGONAL_FORCATION);
	}
	
	private int stonesAmountRound = 0;

	private SogoMove takeMove;
	
	@Override
	public void generateNextMove(SogoGameConsole c) {
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();

		
		long bp1 = GameAnalyzer.getBP1FromGame(g);
		long bp2 = GameAnalyzer.getBP2FromGame(g);

		stonesAmountRound = countStones(bp1, bp2);
		
		
		/*if (stonesAmountRound <= 5)
		{
			SogoMove opening = OpeningDatabase.getMove(g, selfPlayer);
			
			if (opening != null)
			{
				c.updateMove(opening);
				return;
			}
		}*/
		
		
		

		takeMove = moves.get(0);
		
		for (int depth = 4; depth < 10; depth++)
		{
			max_value_toplevel(moves, bp1, bp2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth);
			
			if (c.getTimeLeft() <= 0)
			{
				break;
			}
			else
			{
				c.updateMove(takeMove);

				if (debug_messages && depth >= 6)
					System.out.println("Depth " + depth + " done, took: [" + takeMove.i + "," + takeMove.j + "]" + " Did I begin? " + ((stonesAmountRound % 2) == 0));
				
			}
		}
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
						}
						
						

						
						if (debug_messages)
							System.out.println(m.i + "," + m.j + " = " + value);

						if (value > v && value > -999.9)
						{
							takeMove = m;
							v = value;
						}

						if (v >= beta) {
							
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
		if ((remainingDepth <= 0) || GameAnalyzer.hasGameEnded(bp2))
		{
			return evaluateOddEven(bp1, bp2, oldBp1);
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

	    		final double value = max_value(bp1, alteredBp1, alpha, beta, (remainingDepth-1), bp1);
	    		v = Math.min(v, value);
	    		if (v <= alpha) {
	    			return v;
	    		}
	    		else {
	    			beta = Math.min(beta, v);
	    		}
	    	}
	    }

	    return v;
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
		 * 1      Even, because 1 % 2 == 0!!!!!!!!!!!!!!!
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
	
	
	public int countStones(long bp1, long bp2)
	{
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
