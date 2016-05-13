package pl.edu.agh.io.pdptw.configuration;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.generation.GreedyGeneration;
import pl.edu.agh.io.pdptw.algorithm.generation.SweepGeneration;
import pl.edu.agh.io.pdptw.algorithm.insertion.GreedyInsertion;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.RegretInsertion;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.objective.TotalDistanceObjective;
import pl.edu.agh.io.pdptw.algorithm.objective.TotalVehiclesObjective;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.optimization.TabuOptimization;
import pl.edu.agh.io.pdptw.algorithm.removal.RandomRemoval;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.removal.ShawRemoval;
import pl.edu.agh.io.pdptw.algorithm.removal.WorstRemoval;
import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
import pl.edu.agh.io.pdptw.algorithm.scheduling.Scheduler;

import com.google.inject.AbstractModule;

@AllArgsConstructor
public class AlgorithmModule extends AbstractModule {
	private final AlgorithmsDescription description;
	
	private static final Map<String, Class<? extends GenerationAlgorithm>> 
		generationAlgorithms = new HashMap<>();
	private static final Map<String, Class<? extends InsertionAlgorithm>> 
		insertionAlgorithms = new HashMap<>();
	private static final Map<String, Class<? extends RemovalAlgorithm>> 
		removalAlgorithms = new HashMap<>();
	private static final Map<String, Class<? extends OptimizationAlgorithm>> 
		optimizationAlgorithms = new HashMap<>();
	private static final Map<String, Class<? extends Objective>> 
		objectives = new HashMap<>();
	private static final Map<String, Class<? extends Scheduler>> 
		schedulers = new HashMap<>();
	
	static {
		generationAlgorithms.put("greedy", GreedyGeneration.class);
		generationAlgorithms.put("sweep", SweepGeneration.class);
		
		insertionAlgorithms.put("greedy", GreedyInsertion.class);
		insertionAlgorithms.put("regret", RegretInsertion.class);
		
		removalAlgorithms.put("random", RandomRemoval.class);
		removalAlgorithms.put("worst", WorstRemoval.class);
		removalAlgorithms.put("shaw", ShawRemoval.class);
		
		optimizationAlgorithms.put("tabu", TabuOptimization.class);
		
		objectives.put("total_distance", TotalDistanceObjective.class);
		objectives.put("total_vehicles", TotalVehiclesObjective.class);
		
		schedulers.put("drive_first", DriveFirstScheduler.class);
	}
	
	@Override
	protected void configure() throws IllegalArgumentException {
		boolean algorithmNamesValid = true;
		final String ERROR_MESSAGE_PATTERN = "\nInvalid %s algorithm name";
		StringBuilder builder = new StringBuilder();
		
		algorithmNamesValid = generationAlgorithms.containsKey(description.getGenerationAlgorithmName());
		if (!algorithmNamesValid) {
			builder.append(String.format(ERROR_MESSAGE_PATTERN, "generation"));
		}
		algorithmNamesValid = insertionAlgorithms.containsKey(description.getInsertionAlgorithmName());
		if (!algorithmNamesValid) {
			builder.append(String.format(ERROR_MESSAGE_PATTERN, "insertion"));
		}
		algorithmNamesValid = removalAlgorithms.containsKey(description.getRemovalAlgorithmName());
		if (!algorithmNamesValid) {
			builder.append(String.format(ERROR_MESSAGE_PATTERN, "removal"));
		}
		algorithmNamesValid = optimizationAlgorithms.containsKey(description.getOptimizationAlgorithmName());
		if (!algorithmNamesValid) {
			builder.append(String.format(ERROR_MESSAGE_PATTERN, "optimization"));
		}
		algorithmNamesValid = objectives.containsKey(description.getObjectiveName());
		if (!algorithmNamesValid) {
			builder.append(String.format(ERROR_MESSAGE_PATTERN, "objective"));
		}
		algorithmNamesValid = schedulers.containsKey(description.getSchedulerName());
		if (!algorithmNamesValid) {
			builder.append(String.format(ERROR_MESSAGE_PATTERN, "scheduling"));
		}
		
		if (!algorithmNamesValid) {
			throw new IllegalArgumentException(builder.toString());
		}
		
		bind(GenerationAlgorithm.class).to(
				generationAlgorithms.get(description.getGenerationAlgorithmName()));
		bind(InsertionAlgorithm.class).to(
				insertionAlgorithms.get(description.getInsertionAlgorithmName()));
		bind(RemovalAlgorithm.class).to(
				removalAlgorithms.get(description.getRemovalAlgorithmName()));
		bind(OptimizationAlgorithm.class).to(
				optimizationAlgorithms.get(description.getOptimizationAlgorithmName()));
		bind(Objective.class).to(
				objectives.get(description.getObjectiveName()));
	}

}
