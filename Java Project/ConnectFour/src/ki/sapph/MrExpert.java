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

public class MrExpert implements SogoPlayer {

	private Player p;
	private SogoGameConsole c;

	private boolean doneFirstMove = false;
	
	private static final double win_p = 1000.0 / 1.0;
	private static final double triple_p = 20.0 / 1.0;
	private static final double double_p = 2.0 / 1.0;
	private static final double single_p = 1.0 / 1.0;
	
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

	private int stonesAmountRound = 0;
	
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
					
					curDepth = depth;
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
				
				
				
				for (int depth = 2; depth <= 6; depth += 1)
				{
					//boolean[][][] b1s = GameAnalyzer.getB1sFromGame(g);
					//boolean[][][] b2s = GameAnalyzer.getB2sFromGame(g);

					long bp1 = GameAnalyzer.getBP1FromGame(g);
					long bp2 = GameAnalyzer.getBP2FromGame(g);
					
					stonesAmountRound = countStones(bp1, bp2);
stonesAmountRound = countStones(bp1, bp2);
					
					if (stonesAmountRound == 4 || stonesAmountRound == 5)
					{
						return;
					}
					
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
					
					curDepth = depth;
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
					else if (val > -999.0)
					{
						c.updateMove(new SogoMove(takeIndex % 4, takeIndex / 4));
					}
					else
					{
					}
					
					
					
					int amountOverMinus1000 = 0;
					for (int i = 0; i < 16; i++)
					{
						if (calcMoves[i] < -999.0)
						{
							amountOverMinus1000++;
						}
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
					
					
					
					
					if (safeWin != -1)
					{
						break;
					}
					else
					{
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
	private int curDepth = 0;
	
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
		
		

		//if (depth <= 0 || GameAnalyzer.hasGameEnded(isMax ? bp2 : bp1))
		//{
			double val;
			if (GameAnalyzer.hasGameEnded(isMax ? bp2 : bp1))
			{
				val = evaluateGame(bp1, bp2);
				return val;
			}
			else if (depth <= 0)
			{
				val = evaluateGame(bp1, bp2);
				return val;
			}
			
			//double val = evaluateGame(bp1, bp2);
		//}
		//else
		//{
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
					
					if (childValue >= 900.0 && firstNode)
					{
						showAll = true;
					}
					
					if (firstNode)
					{
						if (Math.abs(calcMoves[i] - childValue) >= 899*2.0 && Math.abs(calcMoves[i] - childValue) <= 901*2.0)
						{
							
						}
						
						calcMoves[i] = childValue;
						
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
		//}
		
	}
	
	
	
	private boolean[] threatPoses = new boolean[16];
	
	/**
	 * 1 = p1 threat
	 * 2 = p2 threat
	 * 3 = stone
	 * 0 = air
	 * 
	 * 
	 */
	private byte[][] threats = new byte[16][4];
	
	
	
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
	
	public int countOpenLines(long bp1, long bp2)
	{
		int amount = GameAnalyzer.longLines.length;
		
		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			long p1Here = GameAnalyzer.longLines[i] & bp1;
			long p2Here = GameAnalyzer.longLines[i] & bp2;
			if (p1Here != 0x0L && p2Here != 0x0L)
			{
				amount--;
			}
		}
		
		return amount;
	}
	
	
	public double evaluateMilton(long bp1, long bp2, boolean turnP1)
	{
		/*boolean[] threats = new boolean[16];
		for (int i = 0; i < threats.length; i++)
		{
			threats[i] = false;
		}*/
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < threats[i].length; j++)
				{
					threats[i][j] = 0;
				}
				threatPoses[i] = false;
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
									threats[j][floor] = 0;
									break;
								}
								else
								{
									threats[j][floor] = 3;
								}
							}
						}
						
						threatPoses[j] = true;
						threats[j][floor] = pThreat ? (byte)1 : (byte)2;
						break;
					}
				}
			}
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
		
		double result = 0;
		
		double[] floors = new double[] {2, 4, 3, 1.5};
		
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < threats[i].length; j++)
				{
					if (threats[i][j] == 1 && (j == 0 || threats[i][j - 1] != 2) && (turnP1 == true || (j >= 1 && (threats[i][j - 1] != 3))))
					{
						result += floors[j];
					}
					else if (threats[i][j] == 2 && (j == 0 || threats[i][j - 1] != 1) && (turnP1 == true || (j >= 1 && (threats[i][j - 1] != 3))))
					{
						result -= floors[j];
					}
				}
			}
		}
		
		
		
		//int stonesAmount = countStones(bp1, bp2);
		//int openLinesAmount = countOpenLines(bp1, bp2);
		int potentialFours = potentialFours(bp1, bp2);
		
		
		if (stonesAmountRound <= 4)
		{
			return evaluateGame(bp1, bp2);
		}
		else
		{
			return result;
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
	
	
	private int potentialFours(long bp1, long bp2)
	{
		int result = 0;
		
		for (int i = 0; i < GameAnalyzer.longLines.length; i++)
		{
			long p1Here = bp1 & GameAnalyzer.longLines[i];
			long p2Here = bp2 & GameAnalyzer.longLines[i];
			
			if (p1Here != 0x0L && p2Here == 0x0L)
			{
				result += 1;
			}
			else if (p1Here == 0x0L && p2Here != 0x0L)
			{
				result += -1;
			}
		}
		
		return result;
	}
	
	private byte recThreatning(boolean turnP1)
	{
		byte turnPlayer = turnP1 ? (byte)1 : 2;
		byte turnOtherPlayer = turnP1 ? (byte)2 : 1;
		
		
		
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < threats[i].length; j++)
				{
					//eigene akute Drohung
					if (j == 0 && threats[i][0] == turnPlayer)
					{
						return 1;
					}
					else if (threats[i][j] == turnPlayer && j > 0 && threats[i][j - 1] == 3)
					{
						return 1;
					}
				}

				
				//beliebiger Zug
				
				//eigene Drohung
				
				//feindliche Drohung - loose
				
				
			}
		}
		
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < threats[i].length; j++)
				{
					//feindliche akute Drohung
					if (j == 0 && threats[i][0] == turnOtherPlayer)
					{
						threats[i][j] = 3;
						return (byte)-recThreatning(!turnP1);
					}
					else if (threats[i][j] == turnOtherPlayer && j > 0 && threats[i][j - 1] == 3)
					{
						threats[i][j] = 3;
						return (byte)-recThreatning(!turnP1);
					}
				}
			}
		}
		
		
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < 3; j++)
				{
					if ((j == 0 || threats[i][j - 1] == 3) && threats[i][j] == 0 && threats[i][j + 1] == 0)
					{
						threats[i][j] = 3;
						return (byte)-recThreatning(!turnP1);
					}
				}
			}
		}
		
		
		//eigene drohung kaputt machen
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < 3; j++)
				{
					if ((j == 0 || threats[i][j - 1] == 3) && threats[i][j] == 0 && threats[i][j + 1] == turnPlayer)
					{
						threats[i][j] = 3;
						return (byte)-recThreatning(!turnP1);
					}
				}
			}
		}
		
		//feindliche drohung aktiv machen
		for (int i = 0; i < threatPoses.length; i++)
		{
			if (threatPoses[i])
			{
				for (int j = 0; j < 3; j++)
				{
					if ((j == 0 || threats[i][j - 1] == 3) && threats[i][j] == 0 && threats[i][j + 1] == turnOtherPlayer)
					{
						//threats[i][j] = 3;
						return -1;
					}
				}
			}
		}
		
		
		
		return 0;
	}
	
	
	
	// currently active Evaluate Method
	public double evaluateGame(long bp1, long bp2)
	{
		List<Byte> all3LinesP1 = new ArrayList<Byte>();
		List<Byte> all3LinesP2 = new ArrayList<Byte>();
		
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
					all3LinesP1.add((byte) i);
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
					all3LinesP2.add((byte) i);
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
		
		
		// Checks the field for Dilemmas 
		if(checkDilemma(all3LinesP1, bp1, bp2)){
			return win_p;
		}else if(checkDilemma(all3LinesP2, bp2, bp1)){
			return -win_p;
		}else{
			return res;
		}
	}
	
	
	
	// Check for a Dilemma, true if bp1 argument has one
	public boolean checkDilemma(List<Byte> all3Lines, long bp1, long bp2){
		
		long stonePos1;
		long stonePos2;
		
		long curLongLine1;
		long curLongLine2;
		
		// compares all 3Lines
		for(int i=0; i<all3Lines.size()-1; i++){
			for(int j=i+1; j<all3Lines.size(); j++){

				curLongLine1 = GameAnalyzer.longLines[all3Lines.get(i)];
				curLongLine2 = GameAnalyzer.longLines[all3Lines.get(j)];
				
				stonePos1 = curLongLine1 & bp1;
				stonePos1 = stonePos1 ^ curLongLine1;
				
				
				
				stonePos2 = curLongLine2 & bp1;
				stonePos2 = stonePos2 ^ curLongLine2;
				// stonePos now keeps only a single 1 at missing place
				
				// Make sure Pos1 != Pos2
				long testPos = stonePos1 & stonePos2;
				if(testPos == 0){
					
					if(fieldPlayable(stonePos1, bp1, bp2)
						&& fieldPlayable(stonePos2, bp1, bp2)){
						return true;
					}
				}		
			}
		}		
		
		return false;
	}
	
	
	// checks if the given stone position can be filled
	public boolean fieldPlayable(long stonePos, long bp1, long bp2){
		
		// checks if the stone is on the lowest level
		if(stonePos < 0XFFFFL){
			return true;
		}else{
			
			stonePos = stonePos >> 16;
			
			if((stonePos & bp1) != 0 || (stonePos & bp2) != 0){
				return true;
			}			
		}

		return false;
	}
	
	
	
	
	
	
	
}
