package testing;

import android.graphics.Point;
import junit.framework.TestCase;
import penny.fourrow.logic.GameField;

/**
 * Created with IntelliJ IDEA.
 * User: Jens
 * Date: 15/05/12
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class GameFieldTest extends TestCase {

    int amountTestRowColumns = 9;
    Point topLeft = new Point(0,0);
    Point middle = new Point(4,4);
    Point bottomRight = new Point(8,8);
    Point validMove = new Point(5,4);
    int testValue = 20;
    GameField gameField = new GameField(amountTestRowColumns, amountTestRowColumns);

    @Override
    public void setUp(){
    }

    public void testInitializeFirstMove() throws Exception {
        gameField.initializeFirstMove(middle, testValue);
    }

    //TODO: refactor naar domme domove, refactor code die checkt naar iets anders hieruit
    public void testDoMove(){
        assertEquals(gameField.doMove(topLeft, testValue), false);
        assertEquals(gameField.doMove(middle, testValue), false);
        assertEquals(gameField.doMove(bottomRight, testValue), false);
        assertEquals(gameField.doMove(validMove, testValue), true);
    }
    public void testGetValueAt(){
        int grabValue = gameField.getValueAtPosition(middle);
        assertEquals(grabValue, testValue);
        grabValue = gameField.getValueAtPosition(validMove);
        assertEquals(grabValue, testValue);
        grabValue = gameField.getValueAtPosition(topLeft);
        assertEquals(grabValue, 0);
        grabValue = gameField.getValueAtPosition(bottomRight);
        assertEquals(grabValue, 0);
    }

    public void testGetFirstPointOnRow() throws Exception {
        Point grabValue = gameField.getFirstPointOnColumn(4);
        assertEquals(grabValue, middle);
        grabValue = gameField.getFirstPointOnColumn(validMove.x); //column, moet valid move zijn
        assertEquals(grabValue, validMove);
        grabValue = gameField.getFirstPointOnColumn(0);
        assertEquals(grabValue, 0);
        grabValue = gameField.getFirstPointOnColumn(8);
        assertEquals(grabValue, 0);
    }

    public void testGetFirstPointOnColumn() throws Exception {
        Point grabValue = gameField.getFirstPointOnColumn(4);
        assertEquals(grabValue, middle);
        grabValue = gameField.getFirstPointOnColumn(validMove.x); //column, moet valid move zijn
        assertEquals(grabValue, middle);
        grabValue = gameField.getFirstPointOnColumn(0);
        assertEquals(grabValue, 0);
        grabValue = gameField.getFirstPointOnColumn(8);
        assertEquals(grabValue, 0);
    }

    public void testResetGameField() throws Exception {
        gameField.resetGameField();
        for (int i = 0; i < amountTestRowColumns; i++){
            for (int j = 0; j < amountTestRowColumns; j++){
                assertEquals(gameField.getValueAtPosition(new Point(i,j)), 0);
            }
        }
    }
}
