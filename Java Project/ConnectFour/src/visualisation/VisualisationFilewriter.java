package visualisation;

import java.util.ArrayList;
import java.util.List;

import game.FieldValue;

public class VisualisationFilewriter {

	private int fieldWidth = 1;
	private int fieldDepth = 1;
	
	private List<FieldValue> fields;
	
	public VisualisationFilewriter()
	{
		fields = new ArrayList<FieldValue>();
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
	
	public void WriteFields(List<FieldValue> fields)
	{
		
	}
}
