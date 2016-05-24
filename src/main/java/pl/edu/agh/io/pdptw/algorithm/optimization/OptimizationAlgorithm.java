package pl.edu.agh.io.pdptw.algorithm.optimization;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;

public interface OptimizationAlgorithm {
	void optimize(Solution solution, Configuration configuration);
}
