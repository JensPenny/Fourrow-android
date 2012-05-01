package support;

import android.graphics.Point;

public class PointPair {
	
	private Point point1;
	private Point point2;
	
	public PointPair(){
		point1 = new Point();
		point2 = new Point();
	}
	
	public Point getFirstPoint(){
		return point1;
	}
	public Point getSecondPoint(){
		return point2;
	}
	public void setFirstPoint(Point firstPoint){
		point1 = firstPoint;
	}
	public void setSecondPoint(Point secondPoint){
		point2 = secondPoint;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Point 1: x " + point1.x);
		builder.append(" y " + point1.y);
		builder.append('\n');
		builder.append("Point 2: x " + point2.x);
		builder.append(" y " + point2.y);
		builder.append('\n');
		return builder.toString();
	}
}
