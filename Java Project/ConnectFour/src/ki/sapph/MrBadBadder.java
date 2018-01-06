package ki.sapph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import util.GameAnalyzer;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.player.SogoPlayer;

public class MrBadBadder implements SogoPlayer {
	
	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	private static int DEPTH = 5;
	
	private int mDepth = 0;
	private Random mRnd;
	private SogoGameConsole c;
	
	public MrBadBadder() {
		this(DEPTH);
	}
	
	public MrBadBadder(int depth) {
		this(depth, System.currentTimeMillis());
	}
	
	public MrBadBadder(int depth, final long seed) {
		mRnd  = new Random(seed);
		mDepth = depth;
	}

	@Override
	public void initialize(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateNextMove(SogoGameConsole c) {
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		final List<SogoMove> bestmoves = new ArrayList<SogoMove>();

		c.updateMove(moves.get(0));
		
		double maxscore = Double.NEGATIVE_INFINITY;

		long bp1 = GameAnalyzer.getBP1FromGame(g);
		long bp2 = GameAnalyzer.getBP2FromGame(g);
		
		
		
		for (int moveIndex = 0; moveIndex < 16; moveIndex++) {
			
			long placedField = 0x0L;

			
			
			long alteredBp1 = bp1;

			for (int z = 0; z < 4; z++)
			{
				placedField = (0x1L << (moveIndex + z * 16));

				if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
				{
					alteredBp1 = bp1 | placedField;
					break;
				}
			}
			
			final double score = min_value(alteredBp1, bp2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, mDepth);

			
			
			

			System.out.println(moveIndex % 4 + "," + moveIndex / 4 + " = " + score);
			
			
			
			
			
			
			
			if (score > maxscore) {
				bestmoves.clear();
				bestmoves.add(new SogoMove(moveIndex % 4, moveIndex / 4));
				maxscore = score;
			} else if (score == maxscore) {
				bestmoves.add(new SogoMove(moveIndex % 4, moveIndex / 4));
			}
		}

		c.updateMove(bestmoves.get(0));
		
		
		SogoMove bestMove = null;
		double bestMoveValue = Double.NEGATIVE_INFINITY;

	        

	}


	public double max_value(long bp1, long bp2, double alpha, double beta, int remainingDepth) {
		//System.out.println("state = " + game.computeStringRepresentation());
		//System.out.println("depth = " + remainingDepth);

		if ((remainingDepth <= 0) || GameAnalyzer.hasGameEnded(bp2)) {
			return evaluateGame(bp1, bp2);
		}

	    double v = Double.NEGATIVE_INFINITY;

        
        for (int i = 0; i < 16; i++)
		{
			long dropPosition = (0x1L << (48 + i));

			if ((bp1 & dropPosition) == 0x0L && (bp2 & dropPosition) == 0x0L && c.getTimeLeft() > 0)
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
				

	            final double value = min_value(alteredBp1, bp2, alpha, beta, (remainingDepth-1));
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

	public double min_value(long bp1, long bp2, double alpha, double beta, int remainingDepth) {
		if ((remainingDepth <= 0) || GameAnalyzer.hasGameEnded(bp1)) {
	    	// we need to be sure to evaluate from the perspective of the MiniMax Agent
	    	return evaluateGame(bp1, bp2);
	    }

	    double v = Double.POSITIVE_INFINITY;
        
        
        
        for (int i = 0; i < 16; i++)
		{
			long dropPosition = (0x1L << (48 + i));

			if ((bp1 & dropPosition) == 0x0L && (bp2 & dropPosition) == 0x0L && c.getTimeLeft() > 0)
			{
				long placedField = 0x0L;

				long alteredBp1 = bp2;
				for (int z = 0; z < 4; z++)
				{
					placedField = (0x1L << (i + z * 16));

					if ((bp1 & placedField) == 0x0L && (bp2 & placedField) == 0x0L)
					{
						alteredBp1 = bp2 | placedField;
						break;
					}
				}
				
				
	            final double value = max_value(bp1, alteredBp1, alpha, beta, (remainingDepth-1));
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

}
