package pl.edu.agh.io.pdptw.algorithm.objective;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class TotalDistanceObjectiveTest {
	private Route route = DataGenerator.generateRoute(10);
	private Objective objective = new TotalDistanceObjective();
	
	@Test
	public void calculateForRouteTest() {
		double expected= 9.0;
		double actual = objective.calculateForRoute(route);
		
		assertEquals(expected, actual, 0.001);
	}
}
