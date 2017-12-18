package util;

public class Vector3 {

	private int x;
	private int y;
	private int z;
	
	public Vector3()
	{
		this(0, 0, 0);
	}
	
	public Vector3(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + "," + y + "," + z + ")";
	}
}
