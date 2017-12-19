package ki.sapph;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.game.SogoGame;

public class MrIdiotGamestate {

	public SogoGame game;
	public boolean isMax;
	public double value;
	
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
