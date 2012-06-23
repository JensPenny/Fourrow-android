package penny.fourrow.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import android.graphics.Point;
import penny.fourrow.gamefield.GameFieldView;

public class GameController implements IGameControl{

	private List<Player> playerList;
	public Player nextPlayer;
	public boolean running = false;
    private GameFieldView gameFieldView;
    public static GameController INSTANCE = new GameController();
	
	private GameController()
	{
		playerList = new LinkedList<Player>();
		nextPlayer = null;
	}

    public void setGameFieldView(GameFieldView gameField){
        this.gameFieldView = gameField;
    }
	
	public void initNewGame(int rows, int columns)
	{
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
			if (gameFieldView.doMove(coords, nextPlayer))
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
	public boolean isGameRunning() {
		return running;
	}
	
	public Logger getLogger(){
		Logger logger = Logger.getLogger(GameController.class.toString());
		return logger;
	}
}
