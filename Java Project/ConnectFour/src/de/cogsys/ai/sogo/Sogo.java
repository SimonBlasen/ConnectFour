package de.cogsys.ai.sogo;

import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.control.TimeCounter;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.player.BadPlayer;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.MrRandom;
import de.cogsys.ai.sogo.player.SogoPlayer;
import ki.sapph.MrBadBadder;
import ki.sapph.MrBitwiseTree;
import ki.sapph.MrExpert;
import ki.sapph.MrInefficientTree;
import ki.sapph.MrMoreefficientTree;
import ki.sapph.MrNeurobit;
import ki.sapph.MrNoviceBit;
//import ki.sapph.MrIdiot;
//import ki.sapph.MrInefficientTree;
import visualisation.Visualisation;

public class Sogo {

	public static final long PLAYER_TIMEOUT = 10000;
	public static final long TIMEOUT_CULANCE = 1000;

	public static double weight1 = 0.0;
	public static double weight2 = 4.0;
	public static double weight3 = 3.0;
	public static double weight4 = 1.0;

	public static int depthStart = 2;
	public static int depthAdd = 1;
	
	

	public static int c_weStartedDiagonalTrueWin = 0;
	public static int c_weStartedDiagonalTrueLose = 0;
	public static int c_weStartedDiagonalFalseWin = 0;
	public static int c_weStartedDiagonalFalseLose = 0;
	public static int c_otherStartedDiagonalTrueWin = 0;
	public static int c_otherStartedDiagonalTrueLose = 0;
	public static int c_otherStartedDiagonalFalseWin = 0;
	public static int c_otherStartedDiagonalFalseLose = 0;
	
	public static boolean weStarted = false;
	public static boolean isDiagonal = false;
	
	public static void main(String[] args) {

		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(System.in));

		if (args.length == 6)
		{
			weight1 = Double.valueOf(args[0]);
			weight2 = Double.valueOf(args[1]);
			weight3 = Double.valueOf(args[2]);
			weight4 = Double.valueOf(args[3]);
			
			depthStart = Integer.valueOf(args[4]);
			depthAdd = Integer.valueOf(args[5]);
		}
		
		
		int p1Won = 0;
		int p2Won = 0;
		int draw = 0;
		for (int rounds = 0; rounds < 1000; rounds++)
		{
			//System.out.println("Round: " + rounds);
			
			SogoGame g = new SogoGame();
			
			/*boolean makeDiag = (new Random()).nextBoolean();
			
			if (makeDiag)
			{
				g = g.performMove(new SogoMove(0, 0));//
				g = g.performMove(new SogoMove(3, 0));
				g = g.performMove(new SogoMove(3, 3));//
				g = g.performMove(new SogoMove(0, 3));
				
				System.out.println("Made Diagonal");
			}
			else
			{
				g = g.performMove(new SogoMove(0, 0));//
				g = g.performMove(new SogoMove(3, 3));
				g = g.performMove(new SogoMove(0, 3));//
				g = g.performMove(new SogoMove(3, 0));

				System.out.println("Not Diagonal");
			}*/
			
			
			final SogoPlayer p1;// = new MrNovice(); // new ConsolePlayer(br);
			final SogoPlayer p2;// = new MrInefficientTree(); // new ConsolePlayer(br);
			
			if (rounds % 2 == 0)
			{
				p1 = new MrNoviceBit();
				p2 = new BadPlayer();
				weStarted = false;
			}
			else
			{
				p1 = new BadPlayer();
				p2 = new MrNoviceBit();
				weStarted = true;
			}
			
			isDiagonal = false;
			
			p1.initialize(Player.P1);
			p2.initialize(Player.P2);
			
			
			
			Visualisation visualisation = new Visualisation();
			

			// game loop
			boolean playing = true;
			int turn = 0;
			final TimeCounter tc = new TimeCounter();
			tc.reset();
			while (playing) {
				
				
				if (g.board[0][0][0] == Player.P1 && g.board[3][3][0] == Player.P1)
				{
					isDiagonal = true;
				}
				if (g.board[0][0][0] == Player.P2 && g.board[3][3][0] == Player.P2)
				{
					isDiagonal = true;
				}
				if (g.board[3][0][0] == Player.P2 && g.board[0][3][0] == Player.P2)
				{
					isDiagonal = true;
				}
				if (g.board[3][0][0] == Player.P1 && g.board[0][3][0] == Player.P1)
				{
					isDiagonal = true;
				}
				
				
				turn++;
				//System.out.println("Turn " + turn + ":");
				//System.out.print(g);

				SogoPlayer player;

				if (g.getCurrentPlayer() == Player.P1) {
					player = p1;
				} else {
					player = p2;
				}

				final SogoPlayer currentplayer = player;
				final SogoMove[] m = new SogoMove[1];
				SogoMove selectedMove = null;

				tc.reset();

				final SogoGame sg = new SogoGame(g);

				final Thread thread = new Thread(() -> {
					currentplayer.generateNextMove(new SogoGameConsole() {
						@Override
						public SogoGame getGame() {
							return new SogoGame(sg);
						}

						@Override
						public long getTimeLeft() {
							return Math.max(0, PLAYER_TIMEOUT - tc.valueMilli());
						}

						@Override
						public void updateMove(final SogoMove move) {
							m[0] = move;
						}
					});
				});

				thread.start();
				try {
					thread.join(PLAYER_TIMEOUT + TIMEOUT_CULANCE);
					selectedMove = m[0];
					thread.interrupt();
				} catch (InterruptedException e) {
				}

				if (selectedMove == null || !g.isValidMove(selectedMove)) {
					System.out.println(
							"Player " + g.getCurrentPlayer() + " has selected an invalid move and forfeits the game");
					//System.exit(1);
					break;
				}
				g = g.performMove(selectedMove);
				

				visualisation.Visualize(g);
				
				if (g.ends()) {
					playing = false;
				}
				//System.out.println();
			}


			visualisation.Visualize(g);

			switch (g.result()) {
			case P1:
				if (rounds % 2 == 0)
				{
					//System.out.println("Player wins: " + p1.toString());
					
					if (isDiagonal && weStarted)
					{
						c_weStartedDiagonalTrueLose++;
					}
					else if (isDiagonal == false && weStarted)
					{
						c_weStartedDiagonalFalseLose++;
					}
					else if (isDiagonal && weStarted == false)
					{
						c_otherStartedDiagonalTrueLose++;
					}
					else if (isDiagonal == false && weStarted == false)
					{
						c_otherStartedDiagonalFalseLose++;
					}
					
					p1Won++;
				}
				else
				{
					//System.out.println("Player wins: " + p1.toString());

					if (isDiagonal && weStarted)
					{
						c_weStartedDiagonalTrueWin++;
					}
					else if (isDiagonal == false && weStarted)
					{
						c_weStartedDiagonalFalseWin++;
					}
					else if (isDiagonal && weStarted == false)
					{
						c_otherStartedDiagonalTrueWin++;
					}
					else if (isDiagonal == false && weStarted == false)
					{
						c_otherStartedDiagonalFalseWin++;
					}
					
					p2Won++;
				}
				break;
			case P2:
				if (rounds % 2 == 0)
				{
					//System.out.println("Player wins: " + p2.toString());
					
					if (isDiagonal && weStarted)
					{
						c_weStartedDiagonalTrueWin++;
					}
					else if (isDiagonal == false && weStarted)
					{
						c_weStartedDiagonalFalseWin++;
					}
					else if (isDiagonal && weStarted == false)
					{
						c_otherStartedDiagonalTrueWin++;
					}
					else if (isDiagonal == false && weStarted == false)
					{
						c_otherStartedDiagonalFalseWin++;
					}
					
					p2Won++;
				}
				else
				{
					//System.out.println("Player wins: " + p2.toString());
					
					if (isDiagonal && weStarted)
					{
						c_weStartedDiagonalTrueLose++;
					}
					else if (isDiagonal == false && weStarted)
					{
						c_weStartedDiagonalFalseLose++;
					}
					else if (isDiagonal && weStarted == false)
					{
						c_otherStartedDiagonalTrueLose++;
					}
					else if (isDiagonal == false && weStarted == false)
					{
						c_otherStartedDiagonalFalseLose++;
					}
					
					p1Won++;
				}
				break;
			case NONE:
				System.out.println("Draw!");
				draw++;
				break;
			}
			//System.out.print(g);

			System.out.println("### [" + p1Won + ":" + p2Won + "] Draw: " + draw + " ###");
			
			System.out.println("### ODW: " + c_otherStartedDiagonalTrueWin);
			System.out.println("### ODL: " + c_otherStartedDiagonalTrueLose);
			System.out.println("### ONW: " + c_otherStartedDiagonalFalseWin);
			System.out.println("### ONL: " + c_otherStartedDiagonalFalseLose);
			System.out.println("### SDW: " + c_weStartedDiagonalTrueWin);
			System.out.println("### SDL: " + c_weStartedDiagonalTrueLose);
			System.out.println("### SNW: " + c_weStartedDiagonalFalseWin);
			System.out.println("### SNL: " + c_weStartedDiagonalFalseLose);
			
			/*
			System.out.print("###############################");
			System.out.print("###############################");
			System.out.print("### [" + p1Won + ":" + p2Won + "] Draw: " + draw + " ######");
			System.out.print("###############################");
			System.out.print("###############################");*/
		}
		
		
		
		
	}

}
