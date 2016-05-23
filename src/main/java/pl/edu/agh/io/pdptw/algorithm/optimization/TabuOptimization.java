package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;

public class TabuOptimization implements OptimizationAlgorithm {
	private static final int MAX_ITERATIONS = 100;
	
	@Override
	public void optimize(Solution solution, Configuration configuration) {
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			
		}
	}
	
	private List<Solution> generateNeighbors(Solution solution) {
		
		return new ArrayList<>();
	}

}
