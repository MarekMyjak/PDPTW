package pl.edu.agh.io.pdptw.algorithm.optimization;

import lombok.Data;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Solution;

@Data
public class OptimizationWorker implements Runnable {
	private OptimizationAlgorithm optimization;
	private Solution solution;
	private AdaptiveMemory adaptiveMemory;
	
	public OptimizationWorker(Solution solution, Configuration configuration) {
		this.solution = solution;
		this.optimization = configuration.getAlgorithms().getOptimizationAlgorithm();
		this.optimization.setConfiguration(configuration);
		this.optimization.setSolution(solution);
		System.out.println("worker: solution size: " + solution.getRequests().size());
	}
	
	@Override
	public void run() {
		LoggingUtils.info("Starting optimization");
		optimization.optimize();
		solution = optimization.getSolution();
		adaptiveMemory = optimization.getAdaptiveMemory();
		LoggingUtils.info("Optimization algorithm has been stopped");
	}
	
	public synchronized void stopOptimization() {
		optimization.stopOptimization();
	}
}
