package de.cogsys.ai.sogo.player;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;

/**
 *  Solution for the Sogo game AI competition from Simon Doll and Benedict Röder.
 *  
 * @author Simon Doll
 * @author Benedic Röder
 */

public class AIRöderDoll implements SogoPlayer {
	private static final double SCALE_FACTOR = 0.06;
	private static final double win_p = 1000;
	private static final double triple_p = 20;
	private static final double double_p = 2;
	private static final double single_p = 1;
	
	private boolean debug_messages = false;
	
	private SogoGameConsole c;
	
	public AIRöderDoll() {
		
	}

	@Override
	public void initialize(Player p)
	{
		//does a lot of very important things ;)
	}
	
	private int stonesAmountRound = 0;

	private SogoMove takeMove;
	
	@Override
	public void generateNextMove(SogoGameConsole c) {
		this.c = c;
		final SogoGame g = c.getGame();
		final List<SogoMove> moves = g.generateValidMoves();

		
		long bp1 = getBP1FromGame(g);
		long bp2 = getBP2FromGame(g);

		stonesAmountRound = countStones(bp1, bp2);
		
		

		takeMove = moves.get(0);
		
		for (int depth = 4; depth < 30; depth++)
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
						
						boolean isKiller = killerMove(alteredBp1, bp2);
						
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
		if ((remainingDepth <= 0) || hasGameEnded(bp2))
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
		if ((remainingDepth <= 0) || hasGameEnded(bp1))
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
	
	
	public static double evaluateGame(long bp1, long bp2, boolean selfTurn)
	{
		List<Byte> all3LinesP1 = new ArrayList<Byte>();
		List<Byte> all3LinesP2 = new ArrayList<Byte>();

		double res = 0.0;

		for (int i = 0; i < longLines.length; i++)
		{
			int self = 0;
			int other = 0;

			long p1Here = bp1 & longLines[i];
			long p2Here = bp2 & longLines[i];

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

		if (selfTurn == false)
		{
			boolean hasIt = hasCloseableThreeLine(all3LinesP2, bp1, bp2);
			if (hasIt == false)
			{// Checks the field for Dilemmas 
				if(checkDilemma(all3LinesP1, bp1, bp2)){
					return win_p * (1.0/SCALE_FACTOR);
				}else{
					return res;
				}
			}
			else
			{
				return -win_p * (1.0/SCALE_FACTOR);

			}
		}
		else
		{
			boolean hasIt = hasCloseableThreeLine(all3LinesP1, bp1, bp2);

			if (hasIt)

			{
				return win_p * (1.0/SCALE_FACTOR);
			}
			else
			{
				if(checkDilemma(all3LinesP2, bp2, bp1)){
					return -win_p * (1.0/SCALE_FACTOR);
				}
			}
		}


		return res;
	}

	// Check for a Dilemma, true if bp1 argument has one
	public static boolean checkDilemma(List<Byte> all3Lines, long bp1, long bp2){

		long stonePos1;
		long stonePos2;

		long curLongLine1;
		long curLongLine2;

		// compares all 3Lines
		for(int i=0; i<all3Lines.size()-1; i++){
			for(int j=i+1; j<all3Lines.size(); j++){

				curLongLine1 = longLines[all3Lines.get(i)];
				curLongLine2 = longLines[all3Lines.get(j)];

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

	public static boolean hasCloseableThreeLine(List<Byte> all3Lines, long bp1, long bp2)
	{
		long stonePos1;

		long curLongLine1;

		// compares all 3Lines
		for(int i=0; i<all3Lines.size()-1; i++){

			curLongLine1 = longLines[all3Lines.get(i)];

			stonePos1 = curLongLine1 & bp1;
			stonePos1 = stonePos1 ^ curLongLine1;



			// stonePos now keeps only a single 1 at missing place

			// Make sure Pos1 != Pos2
			long testPos = stonePos1;
			if(testPos == 0){

				if(fieldPlayable(stonePos1, bp1, bp2)){
					return true;
				}
			}		
		}		

		return false;
	}


	// checks if the given stone position can be filled
	public static boolean fieldPlayable(long stonePos, long bp1, long bp2){

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
	
	
	public double evaluateOddEven(long bp1, long bp2, long oldBp1)
	{
		boolean isEnemyKiller = killerMove(bp2, oldBp1);
		
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

		for (int i = 0; i < longLines.length; i++)
		{
			long hereP1 = (bp1 & longLines[i]) ^ longLines[i];
			long hereP2 = (bp2 & longLines[i]) ^ longLines[i];
			
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
		
		return (difference * 100.0) + evaluateGame(bp1, bp2, oldBp1 == bp1) * SCALE_FACTOR;
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
	
	// lines that contain a win
			public static final long[] longLines = new long[] {
					0x000000000000000FL, //0
					0x00000000000000F0L,
					0x0000000000000F00L,
					0x000000000000F000L,
					0x00000000000F0000L,
					0x0000000000F00000L,
					0x000000000F000000L,
					0x00000000F0000000L,
					0x0000000F00000000L,
					0x000000F000000000L,
					0x00000F0000000000L,
					0x0000F00000000000L,
					0x000F000000000000L,
					0x00F0000000000000L,
					0x0F00000000000000L,
					0xF000000000000000L, //15

					0x0000000000001111L, //16
					0x0000000000002222L,
					0x0000000000004444L,
					0x0000000000008888L,
					0x0000000011110000L,
					0x0000000022220000L,
					0x0000000044440000L,
					0x0000000088880000L,
					0x0000111100000000L,
					0x0000222200000000L,
					0x0000444400000000L,
					0x0000888800000000L,
					0x1111000000000000L,
					0x2222000000000000L,
					0x4444000000000000L,
					0x8888000000000000L, //31

					0x0001000100010001L, //32
					0x0002000200020002L,
					0x0004000400040004L,
					0x0008000800080008L,
					0x0010001000100010L,
					0x0020002000200020L,
					0x0040004000400040L,
					0x0080008000800080L,
					0x0100010001000100L,
					0x0200020002000200L,
					0x0400040004000400L,
					0x0800080008000800L,
					0x1000100010001000L,
					0x2000200020002000L,
					0x4000400040004000L,
					0x8000800080008000L, //47

					0x0008000400020001L,
					0x0080004000200010L,
					0x0800040002000100L,
					0x8000400020001000L,
					0x0001000200040008L,
					0x0010002000400080L,
					0x0100020004000800L,
					0x1000200040008000L,

					0x1000010000100001L,
					0x2000020000200002L,
					0x4000040000400004L,
					0x8000080000800008L,
					0x0001001001001000L,
					0x0002002002002000L,
					0x0004004004004000L,
					0x0008008008008000L,

					0x0000000000008421L,
					0x0000000084210000L,
					0x0000842100000000L,
					0x8421000000000000L,
					0x0000000000001248L,
					0x0000000012480000L,
					0x0000124800000000L,
					0x1248000000000000L,

					0x8000040000200001L,
					0x0001002004008000L,
					0x1000020000400008L,
					0x0008004002001000L,
					};
			
			

			private static long[] oneWallKillerBoardsP1 = new long[]
					{
							// Der 9er Move
							0x9000B, 0xB000BL, 0x1000B000BL, 0x2000B000BL, 0x8000B000BL, 0xA000B000BL, 0x9000B000BL, 0x3000B000BL, 0xA000B000BL, 0x9000B000BL, 0x3000B000BL, 0xB000B000BL, 0xB000B000BL, 0xB000B000BL, 0xF000B000BL, 0x8000B000B000BL, 0x2000B000B000BL, 0x1000B000B000BL, 
							// Der 7er Killer v1
							0x40006000CL, 0x4000E000CL, 0xC000E000CL, 0xE000E000CL, 0x1000E000E000CL, 0xF000E000CL, 
							// Der 7er Killer v2
							0x40006000DL, 0xC0006000DL, 0xE0006000DL, 0xF0006000DL, 0x1000E0006000DL, 
					};
			private static long[] oneWallKillerBoardsP2 = new long[]
					{
						0xF000F00060004L, 0xF000F00040000L, 0xF000E00000000L, 0xF000D00000000L, 0xF000700000000L, 0x7000500000000L, 0x7000600000000L, 0xE000C00000000L, 0xD000500000000L, 0xE000600000000L, 0xD000C00000000L, 0xC000400000000L, 0x6000400000000L, 0x5000400000000L, 0x0L, 0x0L, 0x0L, 0x0L, 
						0xF000B00090000L, 0xB000B00010000L, 0xB000300000000L, 0x3000100000000L, 0x0L, 0x0L, 
						0xF000B00010000L, 0x3000300010000L, 0x1000100000000L, 0x0L, 0x0L, 
					};
			
			
			public static long[] killerBoardsP1 = new long[]
					{
							
							
					};
			public static long[] killerBoardsP2 = new long[]
					{
							
							
					};

			
			private static void fillKillerBoards()
			{
				killerBoardsP1 = new long[oneWallKillerBoardsP1.length * 20];
				killerBoardsP2 = new long[oneWallKillerBoardsP1.length * 20];
				for (int i = 0; i < oneWallKillerBoardsP1.length; i++)
				{
					killerBoardsP1[i * 20 + 0] = oneWallKillerBoardsP1[i];
					killerBoardsP2[i * 20 + 0] = oneWallKillerBoardsP2[i];

					long p1f0 = 0xFL & oneWallKillerBoardsP1[i];
					long p1f1 = (0xF0000L & oneWallKillerBoardsP1[i]) >> 16;
					long p1f2 = (0xF00000000L & oneWallKillerBoardsP1[i]) >> 32;
					long p1f3 = (0xF000000000000L & oneWallKillerBoardsP1[i]) >> 48;
					long p2f0 = 0xFL & oneWallKillerBoardsP2[i];
					long p2f1 = (0xF0000L & oneWallKillerBoardsP2[i]) >> 16;
					long p2f2 = (0xF00000000L & oneWallKillerBoardsP2[i]) >> 32;
					long p2f3 = (0xF000000000000L & oneWallKillerBoardsP2[i]) >> 48;
					
					p1f0 = mirrorSingle(p1f0);
					p1f1 = mirrorSingle(p1f1);
					p1f2 = mirrorSingle(p1f2);
					p1f3 = mirrorSingle(p1f3);
					p2f0 = mirrorSingle(p2f0);
					p2f1 = mirrorSingle(p2f1);
					p2f2 = mirrorSingle(p2f2);
					p2f3 = mirrorSingle(p2f3);

					killerBoardsP1[i * 20 + 1] = p1f0 | (p1f1 << 16) | (p1f2 << 32) | (p1f3 << 48);
					killerBoardsP2[i * 20 + 1] = p2f0 | (p2f1 << 16) | (p2f2 << 32) | (p2f3 << 48);

					
					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 2] = (p1f0 << (4)) | (p1f1 << (16 + 4)) | (p1f2 << (32 + 4)) | (p1f3 << (48 + 4));
					killerBoardsP2[i * 20 + 2] = (p2f0 << (4)) | (p2f1 << (16 + 4)) | (p2f2 << (32 + 4)) | (p2f3 << (48 + 4));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 3] = (p1f0 << (4)) | (p1f1 << (16 + 4)) | (p1f2 << (32 + 4)) | (p1f3 << (48 + 4));
					killerBoardsP2[i * 20 + 3] = (p2f0 << (4)) | (p2f1 << (16 + 4)) | (p2f2 << (32 + 4)) | (p2f3 << (48 + 4));
					
					
					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 4] = (p1f0 << (8)) | (p1f1 << (16 + 8)) | (p1f2 << (32 + 8)) | (p1f3 << (48 + 8));
					killerBoardsP2[i * 20 + 4] = (p2f0 << (8)) | (p2f1 << (16 + 8)) | (p2f2 << (32 + 8)) | (p2f3 << (48 + 8));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 5] = (p1f0 << (8)) | (p1f1 << (16 + 8)) | (p1f2 << (32 + 8)) | (p1f3 << (48 + 8));
					killerBoardsP2[i * 20 + 5] = (p2f0 << (8)) | (p2f1 << (16 + 8)) | (p2f2 << (32 + 8)) | (p2f3 << (48 + 8));
				
					
					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 6] = (p1f0 << (12)) | (p1f1 << (16 + 12)) | (p1f2 << (32 + 12)) | (p1f3 << (48 + 12));
					killerBoardsP2[i * 20 + 6] = (p2f0 << (12)) | (p2f1 << (16 + 12)) | (p2f2 << (32 + 12)) | (p2f3 << (48 + 12));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 7] = (p1f0 << (12)) | (p1f1 << (16 + 12)) | (p1f2 << (32 + 12)) | (p1f3 << (48 + 12));
					killerBoardsP2[i * 20 + 7] = (p2f0 << (12)) | (p2f1 << (16 + 12)) | (p2f2 << (32 + 12)) | (p2f3 << (48 + 12));


					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 8] = ((0x1L & p1f0) << (0)) | ((0x2L & p1f0) << (4 - 1)) | ((0x4L & p1f0) << (8 - 2)) | ((0x8L & p1f0) << (12 - 3))
							| ((0x1L & p1f1) << (16 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1)) | ((0x4L & p1f1) << (16 + 8 - 2)) | ((0x8L & p1f1) << (16 + 12 - 3))
							| ((0x1L & p1f2) << (32 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1)) | ((0x4L & p1f2) << (32 + 8 - 2)) | ((0x8L & p1f2) << (32 + 12 - 3))
							| ((0x1L & p1f3) << (48 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1)) | ((0x4L & p1f3) << (48 + 8 - 2)) | ((0x8L & p1f3) << (48 + 12 - 3));
					killerBoardsP2[i * 20 + 8] = ((0x1L & p2f0) << (0)) | ((0x2L & p2f0) << (4)) | ((0x4L & p2f0) << (8 - 2)) | ((0x8L & p2f0) << (12 - 3))
							| ((0x1L & p2f1) << (16 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1)) | ((0x4L & p2f1) << (16 + 8 - 2)) | ((0x8L & p2f1) << (16 + 12 - 3))
							| ((0x1L & p2f2) << (32 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1)) | ((0x4L & p2f2) << (32 + 8 - 2)) | ((0x8L & p2f2) << (32 + 12 - 3))
							| ((0x1L & p2f3) << (48 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1)) | ((0x4L & p2f3) << (48 + 8 - 2)) | ((0x8L & p2f3) << (48 + 12 - 3));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 9] = ((0x1L & p1f0) << (0)) | ((0x2L & p1f0) << (4 - 1)) | ((0x4L & p1f0) << (8 - 2)) | ((0x8L & p1f0) << (12 - 3))
							| ((0x1L & p1f1) << (16 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1)) | ((0x4L & p1f1) << (16 + 8 - 2)) | ((0x8L & p1f1) << (16 + 12 - 3))
							| ((0x1L & p1f2) << (32 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1)) | ((0x4L & p1f2) << (32 + 8 - 2)) | ((0x8L & p1f2) << (32 + 12 - 3))
							| ((0x1L & p1f3) << (48 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1)) | ((0x4L & p1f3) << (48 + 8 - 2)) | ((0x8L & p1f3) << (48 + 12 - 3));
					killerBoardsP2[i * 20 + 9] = ((0x1L & p2f0) << (0)) | ((0x2L & p2f0) << (4 - 1)) | ((0x4L & p2f0) << (8 - 2)) | ((0x8L & p2f0) << (12 - 3))
							| ((0x1L & p2f1) << (16 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1)) | ((0x4L & p2f1) << (16 + 8 - 2)) | ((0x8L & p2f1) << (16 + 12 - 3))
							| ((0x1L & p2f2) << (32 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1)) | ((0x4L & p2f2) << (32 + 8 - 2)) | ((0x8L & p2f2) << (32 + 12 - 3))
							| ((0x1L & p2f3) << (48 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1)) | ((0x4L & p2f3) << (48 + 8 - 2)) | ((0x8L & p2f3) << (48 + 12 - 3));



					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 10] = ((0x1L & p1f0) << (0 + 1)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 1))
							| ((0x1L & p1f1) << (16 + 0 + 1)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 1))
							| ((0x1L & p1f2) << (32 + 0 + 1)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 1))
							| ((0x1L & p1f3) << (48 + 0 + 1)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 1));
					killerBoardsP2[i * 20 + 10] = ((0x1L & p2f0) << (0 + 1)) | ((0x2L & p2f0) << (4 - 1 + 1)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 1))
							| ((0x1L & p2f1) << (16 + 0 + 1)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 1))
							| ((0x1L & p2f2) << (32 + 0 + 1)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 1))
							| ((0x1L & p2f3) << (48 + 0 + 1)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 1));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 11] = ((0x1L & p1f0) << (0 + 1)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 1))
							| ((0x1L & p1f1) << (16 + 0 + 1)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 1))
							| ((0x1L & p1f2) << (32 + 0 + 1)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 1))
							| ((0x1L & p1f3) << (48 + 0 + 1)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 1));
					killerBoardsP2[i * 20 + 11] = ((0x1L & p2f0) << (0 + 1)) | ((0x2L & p2f0) << (4 + 1)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 1))
							| ((0x1L & p2f1) << (16 + 0 + 1)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 1))
							| ((0x1L & p2f2) << (32 + 0 + 1)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 1))
							| ((0x1L & p2f3) << (48 + 0 + 1)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 1));


					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 12] = ((0x1L & p1f0) << (0 + 2)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 2))
							| ((0x1L & p1f1) << (16 + 0 + 2)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 2))
							| ((0x1L & p1f2) << (32 + 0 + 2)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 2))
							| ((0x1L & p1f3) << (48 + 0 + 2)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 2));
					killerBoardsP2[i * 20 + 12] = ((0x1L & p2f0) << (0 + 2)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 2))
							| ((0x1L & p2f1) << (16 + 0 + 2)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 2))
							| ((0x1L & p2f2) << (32 + 0 + 2)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 2))
							| ((0x1L & p2f3) << (48 + 0 + 2)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 2));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 13] = ((0x1L & p1f0) << (0 + 2)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 2))
							| ((0x1L & p1f1) << (16 + 0 + 2)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 2))
							| ((0x1L & p1f2) << (32 + 0 + 2)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 2))
							| ((0x1L & p1f3) << (48 + 0 + 2)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 2));
					killerBoardsP2[i * 20 + 13] = ((0x1L & p2f0) << (0 + 2)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 2))
							| ((0x1L & p2f1) << (16 + 0 + 2)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 2))
							| ((0x1L & p2f2) << (32 + 0 + 2)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 2))
							| ((0x1L & p2f3) << (48 + 0 + 2)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 2));



					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 14] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 3)) | ((0x4L & p1f0) << (8 - 2 + 3)) | ((0x8L & p1f0) << (12 - 3 + 3))
							| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 3)) | ((0x4L & p1f1) << (16 + 8 - 2 + 3)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 3)) | ((0x4L & p1f2) << (32 + 8 - 2 + 3)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 3)) | ((0x4L & p1f3) << (48 + 8 - 2 + 3)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
					killerBoardsP2[i * 20 + 14] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 3)) | ((0x4L & p2f0) << (8 - 2 + 3)) | ((0x8L & p2f0) << (12 - 3 + 3))
							| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 3)) | ((0x4L & p2f1) << (16 + 8 - 2 + 3)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 3)) | ((0x4L & p2f2) << (32 + 8 - 2 + 3)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 3)) | ((0x4L & p2f3) << (48 + 8 - 2 + 3)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 15] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 3)) | ((0x4L & p1f0) << (8 - 2 + 3)) | ((0x8L & p1f0) << (12 - 3 + 3))
							| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 3)) | ((0x4L & p1f1) << (16 + 8 - 2 + 3)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 3)) | ((0x4L & p1f2) << (32 + 8 - 2 + 3)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 3)) | ((0x4L & p1f3) << (48 + 8 - 2 + 3)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
					killerBoardsP2[i * 20 + 15] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 3)) | ((0x4L & p2f0) << (8 - 2 + 3)) | ((0x8L & p2f0) << (12 - 3 + 3))
							| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 3)) | ((0x4L & p2f1) << (16 + 8 - 2 + 3)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 3)) | ((0x4L & p2f2) << (32 + 8 - 2 + 3)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 3)) | ((0x4L & p2f3) << (48 + 8 - 2 + 3)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));


					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 16] = ((0x1L & p1f0) << (0 + 0)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 3))
							| ((0x1L & p1f1) << (16 + 0 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p1f2) << (32 + 0 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p1f3) << (48 + 0 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
					killerBoardsP2[i * 20 + 16] = ((0x1L & p2f0) << (0 + 0)) | ((0x2L & p2f0) << (4 - 1 + 1)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 3))
							| ((0x1L & p2f1) << (16 + 0 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p2f2) << (32 + 0 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p2f3) << (48 + 0 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 17] = ((0x1L & p1f0) << (0 + 0)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 3))
							| ((0x1L & p1f1) << (16 + 0 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p1f2) << (32 + 0 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p1f3) << (48 + 0 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
					killerBoardsP2[i * 20 + 17] = ((0x1L & p2f0) << (0 + 0)) | ((0x2L & p2f0) << (4 - 1 + 1)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 3))
							| ((0x1L & p2f1) << (16 + 0 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
							| ((0x1L & p2f2) << (32 + 0 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
							| ((0x1L & p2f3) << (48 + 0 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));


					p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

					killerBoardsP1[i * 20 + 18] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 0))
							| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 0))
							| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 0))
							| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 0));
					killerBoardsP2[i * 20 + 18] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 0))
							| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 0))
							| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 0))
							| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 0));

					p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
					p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
					p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
					p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
					p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
					p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
					p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
					p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

					killerBoardsP1[i * 20 + 19] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 0))
							| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 0))
							| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 0))
							| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 0));
					killerBoardsP2[i * 20 + 19] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 0))
							| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 0))
							| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 0))
							| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 0));

				}
			}
			
			private static long mirrorSingle(long single)
			{
				if (single == 0x0L)
					return 0x0L;
				else if (single == 0x1L)
					return 0x8L;
				else if (single == 0x2L)
					return 0x4L;
				else if (single == 0x3L)
					return 0xCL;
				else if (single == 0x4L)
					return 0x2L;
				else if (single == 0x5L)
					return 0xAL;
				else if (single == 0x6L)
					return 0x6L;
				else if (single == 0x7L)
					return 0xEL;
				else if (single == 0x8L)
					return 0x1L;
				else if (single == 0x9L)
					return 0x9L;
				else if (single == 0xAL)
					return 0x5L;
				else if (single == 0xBL)
					return 0xDL;
				else if (single == 0xCL)
					return 0x3L;
				else if (single == 0xDL)
					return 0xBL;
				else if (single == 0xEL)
					return 0x7L;
				else if (single == 0xFL)
					return 0xFL;
				else
				{
					System.out.println("One of the above cases should fit :(");
					System.exit(-1);
					return 0x0L;
				}
			}
		
		
		public static boolean hasGameEnded(long bp)
		{
			for (int i = 0; i < longLines.length; i++)
			{
				if ((bp & longLines[i]) == longLines[i])
				{
					return true;
				}
			}
			return false;
		}
		
		public static boolean killerMove(long bp1, long bp2)
		{
			if (killerBoardsP1.length == 0)
			{
				fillKillerBoards();
			}
			
			for (int i = 0; i < killerBoardsP1.length; i++)
			{
				long self = bp1 & killerBoardsP1[i];
				long other = bp2 & killerBoardsP2[i];
				
				if (self == killerBoardsP1[i] && other == 0x0L)
				{
					return true;
				}
			}
			
			return false;
		}
		
		public static long getBP1FromGame(SogoGame g)
		{
			long bp = 0x0L;
			for (int z = 3; z >= 0; z--)
			{
				for (int y = 3; y >= 0; y--)
				{
					for (int x = 3; x >= 0; x--)
					{
						bp = (bp << 1) | (g.board[x][y][z] == g.getCurrentPlayer() ? 0x1L : 0x0L);
					}
				}
			}
			
			return bp;
		}
		
		public static long getBP2FromGame(SogoGame g)
		{
			long bp = 0x0L;
			for (int z = 3; z >= 0; z--)
			{
				for (int y = 3; y >= 0; y--)
				{
					for (int x = 3; x >= 0; x--)
					{
						bp = (bp << 1) | (g.board[x][y][z] == g.getOtherPlayer() ? 0x1L : 0x0L);
					}
				}
			}
			
			return bp;
		}
	
	
	
}
