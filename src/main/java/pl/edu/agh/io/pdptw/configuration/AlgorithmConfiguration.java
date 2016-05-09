package pl.edu.agh.io.pdptw.configuration;

import lombok.Value;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;

import com.google.inject.Inject;

@Value
public class AlgorithmConfiguration {
	GenerationAlgorithm generationAlgorithm;
	InsertionAlgorithm insertionAlgorithm;
	RemovalAlgorithm removalAlgorithm;
	OptimizationAlgorithm optimizationAlgorithm;
	Objective objective;
	
	@Inject
	public AlgorithmConfiguration(GenerationAlgorithm generationAlgorithm,
			InsertionAlgorithm insertionAlgorithm,
			RemovalAlgorithm removalAlgorithm,
			OptimizationAlgorithm optimizationAlgorithm,
			Objective objective) {
		
		this.generationAlgorithm = generationAlgorithm;
		this.insertionAlgorithm = insertionAlgorithm;
		this.removalAlgorithm = removalAlgorithm;
		this.optimizationAlgorithm = optimizationAlgorithm;
		this.objective = objective;
	}
}
