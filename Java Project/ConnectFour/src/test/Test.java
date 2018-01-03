package test;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.SogoPlayer;
import heuristic.Net;
import ki.sapph.MrBitwiseTree;
import ki.sapph.MrMoreefficientTree;
import util.GameAnalyzer;

public class Test {
	
	private static SogoGame[] games = new SogoGame[0];
	
	

	public static void main(String[] args) {
		
		
		MrBitwiseTree bittree = new MrBitwiseTree();
		
		long bp1 = 0XB00087L;
		long bp2 = 0X70L;
		
		List<Byte> all3Lines = new ArrayList<Byte>();
		all3Lines.add((byte) 0);
		all3Lines.add((byte) 5);

		
		System.out.println(bittree.checkDilemma(all3Lines, bp1, bp2));
		
		
		/*
		Net xor = new Net("D:/Dokumente/xor_float.net");
		
		int[] inputs = new int[] {1, 1};
		
		float result = Net.run(xor, inputs);
		
		System.out.println(result);
		
		
		
		/*
		games[0] = new SogoGame();
		games[0].board[0][0][0] = Player.P1;
		games[0].board[0][1][0] = Player.P2;
		games[0].board[2][0][0] = Player.P1;
		games[0].board[3][2][0] = Player.P2;

		games[1] = new SogoGame();
		games[1].board[2][0][0] = Player.P1;
		games[1].board[2][0][1] = Player.P2;
		games[1].board[1][2][0] = Player.P1;
		games[1].board[3][3][0] = Player.P2;*/
		
/*
		
		int takeIndex = 0; // (3) ,(0)
		
		System.out.println("Current player: " + games[0].getCurrentPlayer());
		
		games[0] = games[0].performMove(new SogoMove(takeIndex % 4, takeIndex / 4));

		long gameInLong1 = GameAnalyzer.getBP1FromGame(games[0]);
		long gameInLong2 = GameAnalyzer.getBP2FromGame(games[0]);

		
		
		int moveIndex = 0; //   x=3, y=0
		
		long placedField = 0x0L;
		
			for (int z = 0; z < 4; z++)
			{
				placedField = (0x1L << (moveIndex + z * 16));
				
				if ((gameInLong1 & placedField) == 0x0L && (gameInLong2 & placedField) == 0x0L)
				{
					if (true)
					{
						gameInLong1 |= placedField;
					}
					else
					{
						gameInLong2 |= placedField;
					}
					break;
				}
			}
		
		
		*/
		
		
		
		
		
		
		/*
		
		
		
		
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = 0; z < 4; z++)
				{
					long tempTest = (0x1L << (x + y * 4 + z * 16));
					
					if ((tempTest & gameInLong1) != 0x0L)
					{
						System.out.println("P1 has stone at: (" + x + "," + y + "," + z + ")");
					}
					if ((tempTest & gameInLong2) != 0x0L)
					{
						System.out.println("P2 has stone at: (" + x + "," + y + "," + z + ")");
					}
				}
			}
		}
		
		
		*/
		
/*
		
		for (int gc = 0; gc < games.length; gc++)
		{
			SogoGame g = new SogoGame();
			SogoGame sg1 = new SogoGame(games[gc]);
			SogoGame sg2 = new SogoGame(games[gc]);
			
			
			MrNovice m1 = new MrNovice();
			MrBitwiseTree m2 = new MrBitwiseTree();



			SogoPlayer pl1 = m1;
			SogoPlayer pl2 = m2;
			
			pl1.generateNextMove(new SogoGameConsole() {
				@Override
				public SogoGame getGame() {
					return new SogoGame(sg1);
				}

				@Override
				public long getTimeLeft() {
					return Math.max(0, 1000000L);
				}

				@Override
				public void updateMove(final SogoMove move) {
					
				}
			});
			
			pl2.generateNextMove(new SogoGameConsole() {
				@Override
				public SogoGame getGame() {
					return new SogoGame(sg2);
				}

				@Override
				public long getTimeLeft() {
					return Math.max(0, 1000000L);
				}

				@Override
				public void updateMove(final SogoMove move) {
					
				}
			});		
			
			
			for (int i = 0; i < 16; i++)
			{
				if (m1.calcMoves[i] != m2.calcMoves[i])
				{
					System.out.println("Difference at " + i);
				}
				
				System.out.println("[" + i + "] " + m1.calcMoves[i] + "," + m2.calcMoves[i]);
			}
		}
		
		
		
		
		
		
		*/

				
		
	}

}
