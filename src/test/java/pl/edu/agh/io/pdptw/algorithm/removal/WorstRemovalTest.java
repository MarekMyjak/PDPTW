package pl.edu.agh.io.pdptw.algorithm.removal;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class WorstRemovalTest {
	
	@Test
	public void test() {
		Solution s = DataGenerator.generateSolution(1);
		System.out.println("Before: " + s);
		RemovalAlgorithm alg = new WorstRemoval();
		Configuration config = DataGenerator.generateConfiguration();
		
		Vehicle.setScheduler(new DriveFirstScheduler());
		System.out.println("Worst position: " + alg.findBestRemovalPositions(
				s.getVehicles().get(0), config));
		Request pickup = alg.removeRequestFromSolution(s, config);
		System.out.println("Removed: " + pickup);
		System.out.println("\n\nAfter: " + s);
	}

}
