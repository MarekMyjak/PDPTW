package pl.edu.agh.io.pdptw.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationPolarAngleTest {

	@Test
	public void test() {
		Location l1 = new Location(0, 0);
		Location l2 = new Location(1, 1);
		
		double expected = Math.PI / 4;
		double actual = Location.calculatePolarAngle(l1, l2);
		assertEquals(expected, actual, 0.001);
	}
}
