package penny.fourrow.logic;

import java.util.Observable;

import android.graphics.Point;

public class GameField extends Observable{
	
	public int[][] gameField;
	private EventListenerList endingListenerList = new EventListenerList();
	
	public void addGameFinishedListener(GameFinishedListener listener){
		if (listener != null)
			endingListenerList.add(GameFinishedListener.class, listener);
	}
	public void removeGameFinishedListener(GameFinishedListener listener){
		endingListenerList.remove(GameFinishedListener.class, listener);
	}
	
	private void fireGameFinishedEvent(GameFinishedEvent evt)
	{
		Object[] listeners = endingListenerList.getListenerList();
		for (int i = 0; i < listeners.length; i+=2){
			if (listeners[i]== GameFinishedListener.class){
				((GameFinishedListener)listeners[i+1]).gamefinishedEventHappened(evt);
			}
		}
	}
	
	public GameField(int rows, int columns){
		gameField = new int[rows][columns];
		resetGameField(rows, columns);
	}
	
	public void resetGameField(int rows, int columns)
	{
		//Initialiseer het veld
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < rows; j++)
			{
				gameField[i][j] = 0;
			}
		}
	}
	
	//Only call this for the initialization of the first blocks
	//This bypasses checks
	public void initializeFirstMove(Point coords, int value)
	{
		if (gameField[coords.x][coords.y] == 0)
		{
			gameField[coords.x][coords.y] = value;
			this.setChanged();
			this.notifyObservers(coords);
		}
	}
	
	public boolean doMove(Point coords, int value)
	{
		if (gameField[coords.x][coords.y] == 0 && checkValidNeighbours(coords)){
			gameField[coords.x][coords.y] = value;
			this.setChanged();
			this.notifyObservers(coords);
			
			if (this.checkForWinning(coords))
				fireGameFinishedEvent(new GameFinishedEvent(new Object()));
			return true;
		}
		return false;
	}

	@Override
	public void notifyObservers(Object coords) {
		// TODO Auto-generated method stub
		super.notifyObservers(coords);
	}
	
	private boolean checkValidNeighbours(Point base){
		boolean neighbourfound = false;
		
		if (base.x > 0)
			if(gameField[base.x-1][base.y] != 0)
				neighbourfound = true;
		if (base.y < gameField.length - 1)
			if (gameField[base.x][base.y+1] != 0 )
				neighbourfound = true;
		if (base.y > 0)
			if (gameField[base.x][base.y-1] != 0)
				neighbourfound = true;
		if (base.x < gameField.length - 1)
			if (gameField[base.x+1][base.y] != 0)
				neighbourfound = true;
		
		return neighbourfound;
	}
	
	//TODO: check voor refactor - haal middencode hieruit
	private boolean checkForWinning(Point start)
	{
		boolean hasWon = false;
		
		int baseValue = gameField[start.x][start.y];
		
		hasWon = this.checkHorizontalWinner(start, baseValue);
		if (!hasWon)
			hasWon = this.checkVerticalWinner(start, baseValue);
		if (!hasWon)
			hasWon = this.checkRisingDiagonalWinner(start, baseValue);
		if (!hasWon)
			hasWon = this.checkFallingDiagonalWinner(start, baseValue);
//		Point current = start;
//		int counter = 1;
//		
//		//Check horizontal
//		current.x = current.x + 1;
//		//Check to the left
//		while (gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.x = current.x+1;
//		}
//		//Check to the right
//		current = start;
//		current.x = current.x - 1;
//		while(gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.x = current.x -1;
//		}
//		if (counter >= 4)
//			return true;
//		
//		//Vertical
//		current = start;
//		counter = 1;
//		current.y = current.y + 1;
//		while(gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.y = current.y + 1;
//		}
//		current = start;
//		current.y = current.y - 1;
//		while(gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.y = current.y - 1;
//		}
//		
//		if (counter >= 4)
//			return true;
//		
//		//Diagonal
//		counter = 1;
//		current = start;
//		current.x = current.x + 1;
//		current.y = current.y +1;
//		//Check to the left
//		while (gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.x = current.x+1;
//			current.y = current.y + 1;
//		}
//		//Check to the right
//		current = start;
//		current.x = current.x - 1;
//		current.y = current.y - 1;
//		while(gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.x = current.x -1;
//			current.y = current.y - 1;
//		}
//		if (counter >= 4)
//			return true;
//		
//		//Diagonal
//		counter = 1;
//		current = start;
//		current.x = current.x + 1;
//		current.y = current.y -1;
//		//Check to the left
//		while (gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.x = current.x+1;
//			current.y = current.y - 1;
//		}
//		//Check to the right
//		current = start;
//		current.x = current.x - 1;
//		current.y = current.y + 1;
//		while(gameField[current.x][current.y] == baseValue){
//			counter++;
//			current.x = current.x -1;
//			current.y = current.y + 1;
//		}
//		if (counter >= 4)
//			return true;
		
		return hasWon;
	}
	
	private boolean checkFallingDiagonalWinner(Point start, int baseValue) {
		int correctPlaces = 0;
		int offset = 0;
		
		while (start.y - offset >= 0 && start.x - offset >=0)
		{
			if (gameField[start.x - offset][start.y - offset] == baseValue)
				correctPlaces++;
			else 
				break;
			offset++;
		}
		offset = 1;
		while (start.y + offset < gameField.length  && start.x + offset < gameField.length)
		{
			if (gameField[start.x + offset][start.y + offset] == baseValue)
				correctPlaces++;
			else
				break;
			offset++;
		}
		
		if (correctPlaces >= 4)
			return true;
				
		return false;
	}
	//TODO
	private boolean checkRisingDiagonalWinner(Point start, int baseValue) {
		int correctPlaces = 0;
		int offset = 0;
		
		while (start.y + offset < gameField.length && start.x - offset >=0)
		{
			if (gameField[start.x - offset][start.y + offset] == baseValue)
				correctPlaces++;
			else 
				break;
			offset++;
		}
		offset = 1;
		while (start.y - offset >= 0 && start.x + offset < gameField.length)
		{
			if (gameField[start.x + offset][start.y - offset] == baseValue)
				correctPlaces++;
			else
				break;
			offset++;
		}
		
		if (correctPlaces >= 4)
			return true;
				
		return false;
	}
	private boolean checkVerticalWinner(Point start, int baseValue) {
		int correctPlaces = 0;
		int offset = 0;
		
		while (start.x + offset < gameField.length)
		{
			if (gameField[start.x + offset][start.y] == baseValue)
				correctPlaces++;
			else 
				break;
			offset++;
		}
		offset = 1;
		while (start.x - offset >= 0)
		{
			if (gameField[start.x - offset][start.y] == baseValue)
				correctPlaces++;
			else
				break;
			offset++;
		}
		
		if (correctPlaces >= 4)
			return true;
				
		return false;

	}
	private boolean checkHorizontalWinner(Point start, int baseValue) {
		int correctPlaces = 0;
		int offset = 0;
		
		while (start.y + offset < gameField.length)
		{
			if (gameField[start.x][start.y + offset] == baseValue)
				correctPlaces++;
			else 
				break;
			offset++;
		}
		offset = 1;
		while (start.y - offset >= 0)
		{
			if (gameField[start.x][start.y - offset] == baseValue)
				correctPlaces++;
			else
				break;
			offset++;
		}
		
		if (correctPlaces >= 4)
			return true;
				
		return false;
	}
}
