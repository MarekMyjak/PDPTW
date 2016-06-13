package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import lombok.Data;
import pl.edu.agh.io.pdptw.algorithm.decomposition.DecompositionAlgorithm;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.visualization.VisualizationService;

@Data
public class DecompositionOptimizer implements Runnable {
	private Configuration configuration;
	private DecompositionAlgorithm decomposition;
	private Solution solution;
	private List<OptimizationWorker> workers;
	private VisualizationService visualizationService;
	private AdaptiveMemory adaptiveMemory;
	private AtomicBoolean shouldStop;
	
	public DecompositionOptimizer(Solution solution, Configuration configuration) {
		this.solution = solution;
		this.configuration = configuration;
		this.decomposition = configuration.getAlgorithms().getDecompositionAlgorithm();
		this.workers = new LinkedList<OptimizationWorker>();
		this.visualizationService = new VisualizationService();
		this.adaptiveMemory = new AdaptiveMemory(32, configuration);
		this.shouldStop = new AtomicBoolean(false);
	}
	
	@Override
	public void run() {
		this.shouldStop.set(false);
		
		final int CYCLES = (configuration.isDynamic()) ? Integer.MAX_VALUE : configuration.getDecompositionCycles() - 1;
		final int ITERATIONS_PER_DECOMPOSITION = configuration.getIterationsPerDecomposition();
		int cyclesCounter = 0;
		
		while (!shouldStop.get() && cyclesCounter < CYCLES) {
			cyclesCounter++;
			
			for (int i = 0; i < ITERATIONS_PER_DECOMPOSITION && !shouldStop.get(); i++) {
				workers = new LinkedList<>();
				List<Solution> partialSolutions = decomposition.decompose(solution, configuration);
				ExecutorService executor = Executors.newFixedThreadPool(partialSolutions.size());
				
				for (Solution s : partialSolutions) {
					OptimizationWorker worker = new OptimizationWorker(s, configuration);
					workers.add(worker);
					executor.execute(worker);
				}
				
				executor.shutdown();
				try {
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LoggingUtils.logStackTrace(e);
				}
				
				solution = new Solution(
						workers.stream()
						.map(OptimizationWorker::getSolution)
						.flatMap(s -> s.getVehicles().stream())
						.collect(Collectors.toList()));
				double newObjective = workers.stream()
						.map(w -> w.getSolution())
						.mapToDouble(s -> s.getObjectiveValue())
						.sum();
				
				solution.setObjectiveValue(newObjective);
				LoggingUtils.info("New objective value: " + newObjective);
			}
			
			LoggingUtils.info("A decomposition cycle has been finished");
			
			adaptiveMemory.addSolution(solution);
			adaptiveMemory.update();
			solution = adaptiveMemory.createRandomSolution(0.65, 3);
		}
		
		LoggingUtils.info("Final solution size: " + solution.getVehicles().size());
	}
	
	public void stopOptimization() {
		for (OptimizationWorker w : workers) {
			w.stopOptimization();
		}
		
		this.shouldStop.set(true);
	}
	
	public Thread startThread() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
}
