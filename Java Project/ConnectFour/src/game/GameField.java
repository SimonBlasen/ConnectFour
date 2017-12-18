package game;

public class GameField {

	private int positionWidth = 0;
	private int positionDepth = 0;
	private int positionHeight = 0;
	private int fieldValue = 0;
	
	public GameField()
	{
		this(0, 0, 0);
	}
	
	public GameField(int positionWidth, int positionDepth, int positionHeight)
	{
		this(positionWidth, positionDepth, positionHeight, 0);
	}

	public GameField(int positionWidth, int positionDepth, int positionHeight, FieldValue fieldValue)
	{
		this(positionWidth, positionDepth, positionHeight, fieldValue.getValue());
	}
	
	public GameField(int positionWidth, int positionDepth, int positionHeight, int fieldValue)
	{
		this.positionDepth = positionDepth;
		this.positionHeight = positionHeight;
		this.positionWidth = positionWidth;
		this.fieldValue = fieldValue;
	}
	
	public int getPositionWidth()
	{
		return positionWidth;
	}
	
	public int getPositionDepth()
	{
		return positionDepth;
	}
	
	public int getPositionHeight()
	{
		return positionHeight;
	}
}
