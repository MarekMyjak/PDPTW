package pl.edu.agh.io.pdptw.algorithm.optimization;

import org.junit.Test;

import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class TabuOptimizationNeighborhoodGenerationTest {

	@Test
	public void test() {
		Solution solution = DataGenerator.generateSolution(10);
		System.out.println(solution + "\n\n\n");
		Vehicle.setScheduler(new DriveFirstScheduler());
		Configuration configuration = DataGenerator.generateConfiguration();
		TabuOptimization optimization = new TabuOptimization();
		optimization.generateNeighbors(solution, 3, configuration);//.forEach(s -> System.out.println(s + "\n----------------"));
		
		
	}

}
