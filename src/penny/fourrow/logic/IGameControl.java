package penny.fourrow.logic;

import android.graphics.Point;

public interface IGameControl {
	public void startGame();
	public Player getNextPlayer();
	public GameField getPlayingField();
	public boolean isGameRunning();
	
	public void addPlayer(Player player);
	public void removePlayer(Player player);
	
	public void playerMakesMove(Point coords); //The current player makes a move

}
