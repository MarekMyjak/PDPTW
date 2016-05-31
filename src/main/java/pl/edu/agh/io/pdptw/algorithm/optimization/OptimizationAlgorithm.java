package pl.edu.agh.io.pdptw.algorithm.optimization;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;

public interface OptimizationAlgorithm {
	public Solution optimize(Solution solution, Configuration configuration);
}
