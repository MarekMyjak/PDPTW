package pl.edu.agh.io.pdptw.configuration;

import lombok.Value;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.scheduling.Scheduler;

import com.google.inject.Inject;

@Value
public class AlgorithmConfiguration {
	GenerationAlgorithm generationAlgorithm;
	InsertionAlgorithm insertionAlgorithm;
	RemovalAlgorithm removalAlgorithm;
	OptimizationAlgorithm optimizationAlgorithm;
	Objective objective;
	Scheduler scheduler;
	
	@Inject
	public AlgorithmConfiguration(GenerationAlgorithm generationAlgorithm,
			InsertionAlgorithm insertionAlgorithm,
			RemovalAlgorithm removalAlgorithm,
			OptimizationAlgorithm optimizationAlgorithm,
			Objective objective,
			Scheduler scheduler) {
		
		this.generationAlgorithm = generationAlgorithm;
		this.insertionAlgorithm = insertionAlgorithm;
		this.removalAlgorithm = removalAlgorithm;
		this.optimizationAlgorithm = optimizationAlgorithm;
		this.objective = objective;
		this.scheduler = scheduler;
	}
}
