package visualisation;

import java.util.ArrayList;
import java.util.List;

import de.cogsys.ai.sogo.game.SogoGame;
import de.cogsys.ai.sogo.game.SogoGame.Player;
import game.FieldValue;
import game.GameField;
import main.GameState;

public class Visualisation {

	private VisualisationFilewriter filewriter;

	private List<GameField> fieldValues = new ArrayList<GameField>();
	
	public Visualisation()
	{
		filewriter = new VisualisationFilewriter("unity-visualisation-file.txt");
	}
	
	public void Visualize(GameState state)
	{
		
	}
	
	public void Visualize(SogoGame sogoGame)
	{
		for (int x = 0; x < sogoGame.board.length; x++)
		{
			for (int y = 0; y < sogoGame.board[0].length; y++)
			{
				for (int z = 0; z < sogoGame.board[0][0].length; z++)
				{
					if (sogoGame.board[x][y][z] == Player.P1)
					{
						if (isInAlready(new GameField(x, y, z, FieldValue.PLAYER_1)) == false)
						{
							fieldValues.add(new GameField(x, y, z, FieldValue.PLAYER_1));
						}
					}
					else if (sogoGame.board[x][y][z] == Player.P2)
					{
						if (isInAlready(new GameField(x, y, z, FieldValue.PLAYER_2)) == false)
						{
							fieldValues.add(new GameField(x, y, z, FieldValue.PLAYER_2));
						}
					}
				}
			}
		}
		
		filewriter.WriteFields(fieldValues);
	}
	
	private boolean isInAlready(GameField gameField)
	{
		for (int i = 0; i < fieldValues.size(); i++)
		{
			if (fieldValues.get(i).IsEqual(gameField))
			{
				return true;
			}
		}
		
		return false;
	}
}
