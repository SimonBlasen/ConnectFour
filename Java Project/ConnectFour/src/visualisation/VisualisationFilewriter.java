package visualisation;

public class VisualisationFilewriter {

	private int fieldWidth = 1;
	private int fieldDepth = 1;
	
	public VisualisationFilewriter()
	{
		
	}
	
	public void setFieldWidth(int width)
	{
		fieldWidth = width;
	}
	
	public void setFieldDepth(int depth)
	{
		fieldDepth = depth;
	}
	
	public int getFieldWidth()
	{
		return fieldWidth;
	}
	
	public int getFieldDepth()
	{
		return fieldDepth;
	}
}
