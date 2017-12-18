package game;

public enum FieldValue {
EMPTY(0),PLAYER_1(1),PLAYER_2(-1);
	
	private int value;
	private FieldValue(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}
