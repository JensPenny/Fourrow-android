package penny.fourrow.gamefield;

import java.util.Observable;

import android.graphics.Point;
import penny.fourrow.logic.Direction;
import penny.fourrow.logic.EventListenerList;
import penny.fourrow.logic.GameFinishedEvent;
import penny.fourrow.logic.GameFinishedListener;

//Package level
class GameFieldLogic extends Observable{
	
	private int[][] gameField;
	private EventListenerList endingListenerList = new EventListenerList();
	
	public void addGameFinishedListener(GameFinishedListener listener){
		if (listener != null)
			endingListenerList.add(GameFinishedListener.class, listener);
	}
	public void removeGameFinishedListener(GameFinishedListener listener){
		endingListenerList.remove(GameFinishedListener.class, listener);
	}

    public int getValueAtPosition(Point position){
        return gameField[position.x][position.y];
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
	
	public GameFieldLogic(int rows, int columns){
		gameField = new int[rows][columns];
		resetGameField();
	}
	
	public void resetGameField()
	{
		//Initialiseer het veld
		for (int i = 0; i < gameField.length; i++)
		{
			for (int j = 0; j < gameField[i].length; j++)
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

    //TODO: Moet gerefactored worden met inclusie van animatieframework
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

    public Point getFirstEncounteredPoint(int rowColIndex, Direction direction){
        Point result;
        if (direction == Direction.LEFTTORIGHT || direction == Direction.RIGHTTOLEFT)
        {
            result = getFirstPointOnRow(rowColIndex, direction);
        }
        else {
            result = getFirstPointOnColumn(rowColIndex, direction);
        }
        return result;
    }

    //X = column, Y = row!!!
    private Point getFirstPointOnRow(int rowIndex, Direction direction){
        if (direction == Direction.LEFTTORIGHT){
            for (int i = 0; i < gameField.length; i++){
                Point testedPoint = new Point(i , rowIndex);
                if (getValueAtPosition(testedPoint) != 0){
                    return testedPoint;
                }
            }
        }else if (direction == Direction.RIGHTTOLEFT){
            for (int i = gameField.length-1; i >=0; i--){
                Point testedPoint = new Point(i , rowIndex);
                if (getValueAtPosition(testedPoint) != 0){
                    return testedPoint;
                }
            }
        }
        return null; //Of new point ???
    }
    private Point getFirstPointOnColumn(int columnIndex, Direction direction){

        if (direction == Direction.UPWARDS){
            for (int i = gameField.length-1; i >=0; i--){
                Point testedPoint = new Point(columnIndex, i);
                if (getValueAtPosition(testedPoint) != 0){
                    return testedPoint;
                }
            }
        }else if (direction == Direction.DOWNWARDS){
            for (int i = 0; i < gameField.length; i++){
                Point testedPoint = new Point(columnIndex, i);
                if (getValueAtPosition(testedPoint) != 0){
                    return testedPoint;
                }
            }
        }
        return null;
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
