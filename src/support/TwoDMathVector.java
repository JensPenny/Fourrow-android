package support;

import android.graphics.Point;

/**
 * Created with IntelliJ IDEA.
 * User: Jens
 * Date: 12/05/12
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
public class TwoDMathVector {
    private Point vector = new Point();

    private Point startPoint = new Point();
    private Point endPoint = new Point();

    public TwoDMathVector(Point start, Point end){
        calculateVector(start, end);
    }

    private void calculateVector(Point startPoint, Point endPoint){
        vector.x = endPoint.x - startPoint.x;
        vector.y = endPoint.y - startPoint.y;
    }

    public Point get2DVector(){
        return vector;
    }

    public void setSecondPoint(Point endPoint){
        calculateVector(startPoint, endPoint);
    }
    public void setStartPoint(Point startPoint){
        calculateVector(startPoint, endPoint);
    }

    /*
    public void dotProduct(TwoDMathVector secondVector){
        vector.x = vector.x * secondVector.get2DVector().x;
        vector.y = vector.y * secondVector.get2DVector().y;
    }
    */
}
