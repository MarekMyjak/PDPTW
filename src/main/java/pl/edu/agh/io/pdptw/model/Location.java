package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Location {
    private int x;
    private int y;
    
    @Override 
    public String toString() {
    	return "(" + x + "," + y + ")";
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
    	double distance = calculateDistance(l1, l2);
    	
    	/* Reminder: 
    	 * xDiff / distance == cos(phi) 
    	 * acos(cos(phi)) == phi 
    	 * acos <=> arccos */ 
    	
    	return Math.acos(xDiff / distance);
    }
}
