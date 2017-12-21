package de.cogsys.ai.sogo.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;

public class BadPlayer implements SogoPlayer {
	
	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	private static int DEPTH = 5;
	
	private int mDepth = 0;
	private Random mRnd;
	
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

	@Override
	public void generateNextMove(SogoGameConsole c) {
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();
		final List<SogoMove> bestmoves = new ArrayList<SogoMove>();

		double maxscore = Double.NEGATIVE_INFINITY;

		for (SogoMove m : moves) {
			final double score = min_value(c.getGame().performMove(m), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, mDepth);

			if (score > maxscore) {
				bestmoves.clear();
				bestmoves.add(m);
				maxscore = score;
			} else if (score == maxscore) {
				bestmoves.add(m);
			}
		}

		c.updateMove(bestmoves.get(mRnd.nextInt(bestmoves.size())));
		
		
		SogoMove bestMove = null;
		double bestMoveValue = Double.NEGATIVE_INFINITY;

	        

	}


	public double max_value(final SogoGame game, double alpha, double beta, int remainingDepth) {
		//System.out.println("state = " + game.computeStringRepresentation());
		//System.out.println("depth = " + remainingDepth);

		if ((remainingDepth <= 0) || game.ends()) {
			return evaluateGame(game);
		}

	    double v = Double.NEGATIVE_INFINITY;
        List<SogoMove> moves = game.generateValidMoves();

        for (SogoMove m : moves) {
            final double value = min_value(game.performMove(m), alpha, beta, (remainingDepth-1));
            v = Math.max(v, value);
            if (v >= beta) {
            	//System.out.println("pruning");
                return v;
            }
            else {
            	alpha = Math.max(alpha, v);
            }
        }
        return v;

	}

	public double min_value(final SogoGame game, double alpha, double beta, int remainingDepth) {
		if ((remainingDepth <= 0) || game.ends()) {
	    	// we need to be sure to evaluate from the perspective of the MiniMax Agent
	    	return - evaluateGame(game);
	    }

	    double v = Double.POSITIVE_INFINITY;
        List<SogoMove> moves = game.generateValidMoves();

        for (SogoMove m : moves) {
            final double value = max_value(game.performMove(m), alpha, beta, (remainingDepth-1));
            v = Math.min(v, value);
            if (v <= alpha) {
            	//System.out.println("pruning");
                return v;
            }
            else {
            	beta = Math.min(beta, v);
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
	

}
