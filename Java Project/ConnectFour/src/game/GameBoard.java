package game;

public class GameBoard {

	private int width = 0;
	private int depth = 0;
	private int height= 0;
	
	public GameBoard()
	{
		this(1,1,1);
	}
	
	public GameBoard(int width, int depth, int height)
	{
		this.width = width;
		this.depth = depth;
		this.height = height;
	}
	
	
	public int getWidth()
	{
		return width;
	}
	
	public int getDepth()
	{
		return depth;
	}
	
	public int getHeight()
	{
		return height;
	}
}
