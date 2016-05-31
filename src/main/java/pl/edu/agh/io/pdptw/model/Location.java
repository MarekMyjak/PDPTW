package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString()
public class Location {
    private int x;
    private int y;
    private double polarAngle;    
    
    public Location(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    public void updatePolarAngle(Location coordSystemCenter) {
    	this.polarAngle = calculatePolarAngle(coordSystemCenter, this);
    }
    
    /* "ordinary" Euclidean distance */
    
    public static double calculateDistance(Location l1, Location l2) {
		double xDiff = 0;
		double yDiff = 0;
		
		xDiff = l1.getX()
				- l2.getX();
		yDiff = l1.getY()
				- l2.getY();
		
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
    
    /* We assume that the l1 location becomes 
     * the centre of the coordinate system. */
    
    public static double calculatePolarAngle(Location l1, Location l2) {
    	double xDiff = l2.getX() - l1.getX();
    	double yDiff = l2.getY() - l1.getY();
    	double phi = Math.atan2(yDiff, xDiff);
    	
    	return (phi >= 0) ? phi : phi + 2 * Math.PI;
    }
}
