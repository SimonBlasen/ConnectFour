package util;

public class SappGameField
{
	public boolean b1;
	public boolean b2;
	
	public boolean isAir()
	{
		return b1 == false && b2 == false;
	}
	
	public boolean isP1()
	{
		return b1 && (b2 == false);
	}
	
	public boolean isP2()
	{
		return b2 && (b1 == false);
	}
}
