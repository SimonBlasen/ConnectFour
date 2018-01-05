package de.cogsys.ai.sogo;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.control.TimeCounter;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import de.cogsys.ai.sogo.game.SogoMove;
import de.cogsys.ai.sogo.player.BadPlayer;
import de.cogsys.ai.sogo.player.MrNovice;
import de.cogsys.ai.sogo.player.MrRandom;
import de.cogsys.ai.sogo.player.SogoPlayer;
import ki.sapph.MrBitwiseTree;
import ki.sapph.MrExpert;
import ki.sapph.MrInefficientTree;
import ki.sapph.MrMoreefficientTree;
import ki.sapph.MrNeurobit;
//import ki.sapph.MrIdiot;
//import ki.sapph.MrInefficientTree;
import visualisation.Visualisation;

public class Sogo {

	public static final long PLAYER_TIMEOUT = 3000;
	public static final long TIMEOUT_CULANCE = 1000;

	public static void main(String[] args) {

		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(System.in));

		int p1Won = 0;
		int p2Won = 0;
		int draw = 0;
		for (int rounds = 0; rounds < 100; rounds++)
		{
			System.out.println("Round: " + rounds);
			
			SogoGame g = new SogoGame();
			/*
			g = g.performMove(new SogoMove(2, 0));//
			g = g.performMove(new SogoMove(3, 0));
			g = g.performMove(new SogoMove(3, 0));//
			g = g.performMove(new SogoMove(0, 0));
			g = g.performMove(new SogoMove(0, 3));//
			g = g.performMove(new SogoMove(0, 0));
			g = g.performMove(new SogoMove(0, 3));//
			g = g.performMove(new SogoMove(2, 0));
			g = g.performMove(new SogoMove(3, 3));//
			g = g.performMove(new SogoMove(2, 3));
			g = g.performMove(new SogoMove(2, 3));//
			g = g.performMove(new SogoMove(2, 3));
			g = g.performMove(new SogoMove(3, 3));//
			g = g.performMove(new SogoMove(3, 3));
			g = g.performMove(new SogoMove(1, 3));//
			*/
			
			final SogoPlayer p1;// = new MrNovice(); // new ConsolePlayer(br);
			final SogoPlayer p2;// = new MrInefficientTree(); // new ConsolePlayer(br);
			
			if (rounds % 2 == 0)
			{
				p1 = new MrExpert();
				p2 = new MrBitwiseTree();
			}
			else
			{
				p1 = new MrBitwiseTree();
				p2 = new MrExpert();
			}
			
			p1.initialize(Player.P1);
			p2.initialize(Player.P2);
			
			
			
			Visualisation visualisation = new Visualisation();
			

			// game loop
			boolean playing = true;
			int turn = 0;
			final TimeCounter tc = new TimeCounter();
			tc.reset();
			while (playing) {
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


			//visualisation.Visualize(g);

			switch (g.result()) {
			case P1:
				if (rounds % 2 == 0)
				{
					System.out.println("Player 1 (X) wins");
					p1Won++;
				}
				else
				{
					System.out.println("Player 2 (O) wins");
					p2Won++;
				}
				break;
			case P2:
				if (rounds % 2 == 0)
				{
					System.out.println("Player 2 (O) wins");
					p2Won++;
				}
				else
				{
					System.out.println("Player 1 (X) wins");
					p1Won++;
				}
				break;
			case NONE:
				System.out.println("Draw!");
				draw++;
				break;
			}
			//System.out.print(g);

			System.out.print("###############################");
			System.out.print("###############################");
			System.out.print("### [" + p1Won + ":" + p2Won + "] Draw: " + draw + " ######");
			System.out.print("###############################");
			System.out.print("###############################");
		}
		
		
		
		
	}

}
