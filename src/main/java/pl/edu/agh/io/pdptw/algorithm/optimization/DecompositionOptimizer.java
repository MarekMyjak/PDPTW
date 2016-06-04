package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.decomposition.DecompositionAlgorithm;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.visualization.VisualizationService;

public class DecompositionOptimizer implements Runnable {
	private Configuration configuration;
	private DecompositionAlgorithm decomposition;
	private Solution solution;
	private int cyclesNo;
	private List<OptimizationWorker> workers;
	private VisualizationService visualizationService;
	
	public DecompositionOptimizer(Solution solution, int cyclesNo, Configuration configuration) {
		this.solution = solution;
		this.cyclesNo = cyclesNo;
		this.configuration = configuration;
		this.decomposition = configuration.getAlgorithms().getDecompositionAlgorithm();
		this.workers = new LinkedList<OptimizationWorker>();
		this.visualizationService = new VisualizationService();
	}
	
	@Override
	public void run() {
		for (int i = 0; i < cyclesNo; i++) {
			workers = new LinkedList<>();
			List<Solution> partialSolutions = decomposition.decompose(solution, configuration);
			ExecutorService executor = Executors.newFixedThreadPool(partialSolutions.size());
			
			LoggingUtils.info("Created " + partialSolutions.size() + " partial solutions");
			partialSolutions.forEach(s -> System.out.println(s.getRequests().size()));
			
			for (Solution s : partialSolutions) {
				s.getVehicles().forEach(v -> System.out.println(v.getId()));
				System.out.println("-----------------");
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
			
			for (OptimizationWorker w : workers) {
				w.getSolution().getVehicles().forEach(v -> System.out.println(v.getId()));
				System.out.println("++++++++++++++++++++++");
			}
			
			System.out.println("-------NEW SOLUTION END____---------");
			
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
		
		try {
			visualizationService.makeVisualizationData(solution, configuration);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
		}
	}
	
	public void stopOptimization() {
		for (OptimizationWorker w : workers) {
			w.stopOptimization();
		}
	}
	
	public Thread startThread() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
	
}
