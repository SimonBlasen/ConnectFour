package visualisation;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.game.SogoGame;
import game.FieldValue;
import main.GameState;

public class Visualisation {

	private VisualisationFilewriter filewriter;
	
	public Visualisation()
	{
		filewriter = new VisualisationFilewriter();
	}
	
	public void Visualize(GameState state)
	{
		
	}
	
	public void Visualize(SogoGame sogoGame)
	{
		List<FieldValue> fieldValues = new ArrayList<FieldValue>();
	}
}
