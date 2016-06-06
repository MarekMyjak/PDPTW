package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.List;

import org.junit.Test;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class AdaptiveMemoryCreationTest {

	@Test
	public void test() {
		Configuration configuration = DataGenerator.generateConfiguration();
		AdaptiveMemory adaptiveMemory = new AdaptiveMemory(32, configuration);
		Solution solution = DataGenerator.generateSolution(10);
		Solution curSolution = solution;
		List<Solution> neighbors = TabuOptimization.generateNeighbors(curSolution, 10, 10, configuration);
		neighbors.forEach(n -> adaptiveMemory.addSolution(n));
		
		for (int i = 0; i < 100; i++) {
			curSolution = adaptiveMemory.createRandomSolution(0.65, 2);
			System.out.println(curSolution.getRequests().size());
		}
		
		neighbors.forEach(n -> System.out.println("NNNNNNN: " + n.getRequests().size()));
		neighbors.get(0).getVehicles().forEach(v -> {
			v.getRoute().getRequests().forEach(r -> System.out.println("----id: " + r.getId()));
			System.out.println("---------------");
		});
			
	}

}
