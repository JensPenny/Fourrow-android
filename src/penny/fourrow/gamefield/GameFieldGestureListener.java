package penny.fourrow.gamefield;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import penny.fourrow.gamefield.GameFieldView;
import penny.fourrow.logic.Direction;
import penny.fourrow.logic.GameController;
import support.Vector2D;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Jens
 * Date: 9/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public class GameFieldGestureListener implements GestureDetector.OnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GameFieldView gameField;
    private Logger log = Logger.getLogger("GestureLogger");
    private int pixelsPerCell = 0; //Amount of pixels one cell has - width and height should be the same

    public GameFieldGestureListener(GameFieldView gameField){
        this.gameField = gameField;
        recalculateRelativeCellSize();
    }

    public void recalculateRelativeCellSize(){
        int width = gameField.getConstructedGameFieldTableLayout().getWidth();
        pixelsPerCell = width / 9; //TODO: preferences steken
    }

    public Point calculateRowColumnFromRelativePixelPoint(Point pixel){
        Point rowCol = new Point(0,0);
        rowCol.y = pixel.y / pixelsPerCell;
        rowCol.x = pixel.x / pixelsPerCell;
        return rowCol;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //TODO: refactor + settings
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            Point startPoint = calculateRowColumnFromRelativePixelPoint(new Point((int)e1.getX(), (int)e1.getY()));
            Point endPoint = calculateRowColumnFromRelativePixelPoint(new Point((int)e2.getX(), (int)e2.getY()));

            Vector2D vector = new Vector2D(startPoint, endPoint);

            Point firstEncounteredPoint;
            if (Math.abs(vector.get2DVector().x) > Math.abs(vector.get2DVector().y)){
                if (vector.get2DVector().x > 0){
                    log.info("LeftToRight swipe");
                    log.info("On row: " + (startPoint.y + vector.get2DVector().y/2));
                    int row = startPoint.y + vector.get2DVector().y/2;
                    firstEncounteredPoint = gameField.getFirstEncounteredPoint(row, Direction.LEFTTORIGHT);
                    if (firstEncounteredPoint.x >= 1){
                        firstEncounteredPoint.x -= 1;
                        GameController.INSTANCE.playerMakesMove(firstEncounteredPoint, Direction.LEFTTORIGHT);
                    }
                }else
                {
                    log.info("RightToLeft swipe");
                    log.info("On row: " + (startPoint.y + vector.get2DVector().y/2));
                    int row = startPoint.y + vector.get2DVector().y/2;
                    firstEncounteredPoint = gameField.getFirstEncounteredPoint(row, Direction.RIGHTTOLEFT);
                    firstEncounteredPoint.x += 1;
                    if (firstEncounteredPoint.x < 9){ //Test of dit punt op de border ligt -> TODO: refactor
                        GameController.INSTANCE.playerMakesMove(firstEncounteredPoint, Direction.RIGHTTOLEFT);
                    }
                }
            }else{
                if (vector.get2DVector().y > 0){
                    log.info("TopToBot swipe");
                    log.info("On column: " + (startPoint.x + vector.get2DVector().x/2));
                    int column = startPoint.x + vector.get2DVector().x/2;
                    firstEncounteredPoint = gameField.getFirstEncounteredPoint(column, Direction.DOWNWARDS);
                    firstEncounteredPoint.y -= 1;
                    if(firstEncounteredPoint.y >= 0){
                        GameController.INSTANCE.playerMakesMove(firstEncounteredPoint, Direction.DOWNWARDS);
                    }
                }else {
                    log.info("BotToTop swipe");
                    log.info("On column: " +(startPoint.x + vector.get2DVector().x/2));
                    int column = startPoint.x + vector.get2DVector().x/2;
                    firstEncounteredPoint = gameField.getFirstEncounteredPoint(column, Direction.UPWARDS);
                    firstEncounteredPoint.y += 1;
                    if (firstEncounteredPoint.y < 9){
                        GameController.INSTANCE.playerMakesMove(firstEncounteredPoint, Direction.UPWARDS);
                    }
                }

            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }
}
