package pl.edu.agh.io.pdptw.algorithm.optimization;


import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class AdaptiveMemoryContainsSolutionTest {

	@Test
	public void test() {
		Request p1 = new PickupRequest(1, new Location(0, 0), 1, 0, 50, 1);
		Request d1 = new DeliveryRequest(2, new Location(0, 0), -1, 0, 50, 1);
		p1.setSibling(d1);
		d1.setSibling(p1);
		Request p2 = new PickupRequest(1, new Location(0, 0), 1, 0, 50, 1);
		Request d2 = new DeliveryRequest(2, new Location(0, 0), -1, 0, 50, 1);
		p1.setSibling(d2);
		d1.setSibling(p2);
		
		Route r1 = new Route(Arrays.asList(p1, d1));
		Route r2 = new Route(Arrays.asList(p2, d2));
		
		Vehicle v1 = new Vehicle("truck", 200, new Location(1, 2));
		v1.setRoute(r1);
		Vehicle v2 = new Vehicle("plane", 220, new Location(1, 3));
		v2.setRoute(r2);
		
		Solution s1 = new Solution(Arrays.asList(v1));
		Solution s2 = new Solution(Arrays.asList(v2));
		
		AdaptiveMemory am = new AdaptiveMemory(2, DataGenerator.generateConfiguration());
		am.addSolution(s1);
		
		Assert.assertTrue(am.contains(s1));
		Assert.assertTrue(am.contains(s2));
		Assert.assertFalse(am.addSolution(s1));
		Assert.assertFalse(am.addSolution(s2));
	}

}
