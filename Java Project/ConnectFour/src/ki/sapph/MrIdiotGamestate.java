package ki.sapph;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.game.SogoGame;

public class MrIdiotGamestate {

	public SogoGame game;
	public boolean isMax;
	private double value;
	private boolean valueSet = false;
	
	private List<MrIdiotGamestate> children;
	private MrIdiotGamestate parent = null;
	
	
	public MrIdiotGamestate(SogoGame game)
	{
		this(game, true, 0.0);
	}
	
	public MrIdiotGamestate(SogoGame game, boolean isMax)
	{
		this(game, isMax, 0.0);
	}
	
	public MrIdiotGamestate(SogoGame game, boolean isMax, double value)
	{
		this.game = game;
		this.isMax = isMax;
		this.value = value;
		
		children = new ArrayList<>();
	}
	
	
	public double getValue()
	{
		if (valueSet == false)
		{
			return isMax ? 10000.0 : -10000.0;
		}
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
		valueSet = true;
	}
	
	public boolean isValueSet()
	{
		return valueSet;
	}
	
	/**
	 * If a child node found a new value, which is possible to achieve
	 * @param value
	 * @return Whether the value helped or not
	 */
	public boolean updateValue(double value)
	{
		if (valueSet == false || (value > this.value == isMax))
		{
			setValue(value);
			return true;
		}
		return false;
	}
	
	public boolean doesHelp(double value)
	{
		if (valueSet == false)
		{
			return true;
		}
		
		return value > this.value == isMax;
	}
	
	public MrIdiotGamestate getParent()
	{
		return parent;
	}
	
	public void setParent(MrIdiotGamestate parent)
	{
		this.parent = parent;
		if (this.parent != null)
		{
			this.parent.children.add(this);
		}
	}
	
	public List<MrIdiotGamestate> getChildren()
	{
		return children;
	}
}
