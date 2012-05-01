package penny.fourrow.logic;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;

public class GameController implements IGameControl{

	private List<Player> playerList;
	public Player nextPlayer;
	public GameField gameField;
	public boolean running = false;
	
	public GameController()
	{
		playerList = new LinkedList<Player>();
		nextPlayer = null;
		gameField = new GameField(0, 0);
	}
	
	public void initNewGame(int rows, int columns)
	{
		gameField = new GameField(rows, columns);
		for (Player player : playerList) {
			//player.score = 0;
		}
	}
	
	public void addPlayer(Player player)
	{
		playerList.add(player);
	}
	
	public void removePlayer(Player player)
	{
		playerList.remove(player);
	}
	
	public void startGame()
	{
		nextPlayer = ((LinkedList<Player>) playerList).getFirst();
		running = true;
	}
	
	public void playerMakesMove(Point coords)
	{
		if (running)
		{
			if (gameField.doMove(coords, nextPlayer.playerColorResource))
			{
				int index = playerList.indexOf(nextPlayer);
				if (playerList.size() == index + 1)
					nextPlayer = playerList.get(0);
				else{
					Player p = playerList.get(index+1);
					nextPlayer = p;
				}
			}
		}
	}

	public Player getNextPlayer() {
		return nextPlayer;
	}

	@Override
	public GameField getPlayingField() {
		return gameField;
	}

	@Override
	public boolean isGameRunning() {
		return running;
	}
	
}
