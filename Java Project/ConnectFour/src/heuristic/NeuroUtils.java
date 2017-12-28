package heuristic;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.cogsys.ai.sogo.control.SogoGameConsole;
import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoMove;
import game.GameField;
import ki.sapph.MrBitwiseTree;
import util.GameAnalyzer;

public class NeuroUtils {


	public static void randomBoard(int movesAmount) {

		long[] savedP1 = new long[movesAmount];
		long[] savedP2 = new long[movesAmount];


		long p1 = 0L;
		long p2 = 0L;

		boolean isP1 = true;

		MrBitwiseTree ki = new MrBitwiseTree();

		int oldPercent = 0;

		for(int i = 0; i < movesAmount; i++)
		{
			int percent = (i * 100) / movesAmount;
			if (oldPercent != percent)
			{
				oldPercent = percent;
				System.out.println(percent + "%");
			}


			List<Integer> possMoves = new ArrayList<Integer>();

			for (int j = 0; j < 16; j++)
			{
				long dropPosition = (0x1L << (48 + j));

				if ((p1 & dropPosition) == 0x0L && (p2 & dropPosition) == 0x0L)
				{
					possMoves.add(j);
				}
			}


			int toDoMove = possMoves.get((new Random()).nextInt(possMoves.size()));

			if(i % 15 == 0) {

			}
			else {
				toDoMove = ki.generateNextMoveMuchBetter(isP1 ? p1 : p2, isP1 ? p2 : p1);
			}





			long placedField = 0x0L;

			for (int z = 0; z < 4; z++)
			{
				placedField = (0x00000001L << (toDoMove + z * 16));

				if ((p1 & placedField) == 0x0L && (p2 & placedField) == 0x0L)
				{
					if (isP1)
					{
						p1 |= placedField;
					}
					else
					{
						p2 |= placedField;
					}
					break;
				}
			}



			// Save train data

			savedP1[i] = p1;
			savedP2[i] = p2;



			boolean isFinished = GameAnalyzer.hasGameEnded(isP1 ? p1 : p2);

			if (isFinished || ((p1 | p2) == 0xFFFFFFFFFFFFFFFFL))
			{

				p1 = 0x0L;
				p2 = 0x0L;
			}

			isP1 = !isP1;
		}



		//generateTrainData(savedP1, savedP2);
		generateTrainDataDoubleInput(savedP1, savedP2);

		System.out.println("100% Done!");

	}




	public static void generateTrainData(long[] bps1, long[] bps2) {

		MrBitwiseTree ki = new MrBitwiseTree();

		String[] boards = new String[bps1.length];
		double[] expectedValues = new double[bps1.length];
		for(int i = 0; i < bps1.length; i++) {
			boards[i] = generateBoard(bps1[i], bps2[i]);
			expectedValues[i] = ki.evaluateGame(bps1[i], bps2[i]);
		}

		writeTrainData(boards,expectedValues, 64);

	}

	public static void generateTrainDataDoubleInput(long[] bps1, long[] bps2) {

		MrBitwiseTree ki = new MrBitwiseTree();

		String[] boards = new String[bps1.length];
		double[] expectedValues = new double[bps1.length];
		for(int i = 0; i < bps1.length; i++) {
			boards[i] = generateBoardDoubleInput(bps1[i], bps2[i]);
			expectedValues[i] = ki.evaluateGame(bps1[i], bps2[i]);
		}

		writeTrainData(boards,expectedValues, 128);

	}


	public static String generateBoard(long bp1, long bp2) {

		String output = "";

		//generate valid net input:
		for(int i = 63; i >= 0; i--) {
			long currentPos = 0x1L << i;
			if((currentPos & bp1) != 0x0L) {
				output += "1";
			}
			else if((currentPos & bp2) != 0x0L) {
				output += "-1";
			}
			else {
				output += "0";
			}
			if(i > 0) {
				output += " ";
			}
		}

		return output;
	}
	
	public static String generateBoardDoubleInput(long bp1, long bp2) {

		String output = "";

		//generate valid net input:
		for(int i = 63; i >= 0; i--) {
			long currentPos = 0x1L << i;
			if((currentPos & bp1) != 0x0L) {
				output += "1";
			}
			else {
				output += "0";
			}
			if(i >= 0) {
				output += " ";
			}
		}
		
		for(int i = 63; i >= 0; i--) {
			long currentPos = 0x1L << i;
			if((currentPos & bp2) != 0x0L) {
				output += "1";
			}
			else {
				output += "0";
			}
			if(i > 0) {
				output += " ";
			}
		}
		

		return output;
	}



	public static void writeTrainData(String[] boards, double[] expectedValues, int inputsAmount) {

		String filePath = "D:/Dokumente/connectfour_float_test.data";


		try {
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");

			writer.println(boards.length + " " + inputsAmount + " 1");

			for(int i = 0; i < boards.length; i++) {
				writer.println(boards[i]);
				writer.println(expectedValues[i]);
			}

			writer.close();


		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("File coudl not be written");

		}

	}


	public static void main(String[] args) {

		randomBoard(10000);

	}

}
