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
}
