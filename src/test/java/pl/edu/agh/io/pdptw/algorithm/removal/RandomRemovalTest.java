package pl.edu.agh.io.pdptw.algorithm.removal;

import org.junit.Test;

import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class RandomRemovalTest {

	@Test
	public void test() {
		Solution s = DataGenerator.generateSolution(6);
		System.out.println("Before: " + s);
		RemovalAlgorithm alg = new RandomRemoval();
		Configuration config = DataGenerator.generateConfiguration();
		
		Vehicle.setScheduler(new DriveFirstScheduler());
		Request pickup = alg.removeRequestFromSolution(s, config);
		System.out.println("Removed: " + pickup);
		System.out.println("\n\nAfter: " + s);
	}

}
