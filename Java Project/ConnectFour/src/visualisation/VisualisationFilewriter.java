package visualisation;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import game.FieldValue;
import game.GameField;

public class VisualisationFilewriter {

	private String filePath = "";
	
	private int fieldWidth = 4;
	private int fieldDepth = 4;
	
	private List<FieldValue> fields;
	
	public VisualisationFilewriter(String filePath)
	{
		this.filePath = filePath;
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
	
	public void WriteFields(List<GameField> fields)
	{
		try {
			PrintWriter writer = new PrintWriter(filePath, "UTF-8");

			writer.println(fieldWidth + "," + fieldDepth);
			
			for (int i = 0; i < fields.size(); i++)
			{
				writer.println((fields.get(i).getFieldValue() == 1 ? "0" : "1") + "," + fields.get(i).getPositionWidth() + "," + fields.get(i).getPositionDepth() + "," + fields.get(i).getPositionHeight());
			}
			
			writer.close();
			
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		System.out.println("File coudl not be written");

		}
		
	}
}
