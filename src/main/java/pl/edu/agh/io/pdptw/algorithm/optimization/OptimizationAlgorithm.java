package pl.edu.agh.io.pdptw.algorithm.optimization;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;

public interface OptimizationAlgorithm {
	/* before calling optimize() it is necessary
	 * to set the configuration using setConfiguration() */
	
	public Solution optimize();
	public OptimizationAlgorithm setConfiguration(Configuration configuration);
	public OptimizationAlgorithm setSolution(Solution solution);
	public OptimizationAlgorithm setAdaptiveMemory(AdaptiveMemory adaptiveMemory);
	public Solution getSolution();
	public AdaptiveMemory getAdaptiveMemory();
	public void stopOptimization();
}
