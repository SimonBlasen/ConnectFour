package main;

import game.GameBoard;

public class GameState {

	private GameBoard board = null;
	
	public GameState(int boardWidth, int boardDepth, int boardHeight)
	{
		board = new GameBoard(boardWidth, boardDepth, boardHeight);
	}
	
	
	public GameBoard getBoard()
	{
		return board;
	}
}
