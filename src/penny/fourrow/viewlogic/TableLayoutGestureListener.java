package penny.fourrow.viewlogic;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;
import penny.fourrow.logic.GameController;
import support.TwoDMathVector;

import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Jens
 * Date: 9/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public class TableLayoutGestureListener implements GestureDetector.OnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private TableLayout table;
    private GameController controller;
    private Logger log = Logger.getLogger("GestureLogger");
    private int pixelsPerCell = 0; //Amount of pixels one cell has - width and height should be the same

    public TableLayoutGestureListener(TableLayout table, GameController controller){ //GAMECONTROLLER NEEDS A REFACTOR
        this.table = table;
        recalculateRelativeCellSize();
        this.controller = controller;
    }

    public void recalculateRelativeCellSize(){
        int width = table.getWidth();
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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            Point startPoint = calculateRowColumnFromRelativePixelPoint(new Point((int)e1.getX(), (int)e1.getY()));
            Point endPoint = calculateRowColumnFromRelativePixelPoint(new Point((int)e2.getX(), (int)e2.getY()));

            TwoDMathVector vector = new TwoDMathVector(startPoint, endPoint);
            if (Math.abs(vector.get2DVector().x) > Math.abs(vector.get2DVector().y)){
                if (vector.get2DVector().x > 0){
                    log.info("LeftToRight swipe");
                    log.info("On row: " + (startPoint.y + vector.get2DVector().y/2));

                }else
                {
                    log.info("RightToLeft swipe");
                    log.info("On row: " + (startPoint.y + vector.get2DVector().y/2));
                }
            }else{
                if (vector.get2DVector().y > 0){
                    log.info("TopToBot swipe");
                    log.info("On column: " + (startPoint.x + vector.get2DVector().x/2));
                }else {
                    log.info("BotToTop swipe");
                    log.info("On column: " +(startPoint.x + vector.get2DVector().x/2));
                }

            }
            controller.playerMakesMove(startPoint);
            /*
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return true;
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                log.info("Right swipe");
                return true;
            } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY){
                log.info("Bottomup swipe");
                return true;
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY){
                log.info("Toptodown swipe");
                return true;
            }    */
        } catch (Exception e) {
            // nothing
        }
        return false;
    }
}
