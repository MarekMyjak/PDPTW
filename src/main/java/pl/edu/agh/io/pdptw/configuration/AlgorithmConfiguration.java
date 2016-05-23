package pl.edu.agh.io.pdptw.configuration;

import lombok.Data;
import lombok.Value;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.generation.GreedyGeneration;
import pl.edu.agh.io.pdptw.algorithm.generation.SweepGeneration;
import pl.edu.agh.io.pdptw.algorithm.insertion.GreedyInsertion;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.objective.TotalDistanceObjective;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.optimization.TabuOptimization;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.removal.WorstRemoval;
import pl.edu.agh.io.pdptw.algorithm.scheduling.DriveFirstScheduler;
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
	
	public static class AlgorithmConfigurationBuilder {
		GenerationAlgorithm generationAlgorithm = new SweepGeneration();
		InsertionAlgorithm insertionAlgorithm = new GreedyInsertion();
		RemovalAlgorithm removalAlgorithm = new WorstRemoval();
		OptimizationAlgorithm optimizationAlgorithm = new TabuOptimization();
		Objective objective = new TotalDistanceObjective();
		Scheduler scheduler = new DriveFirstScheduler();
		
		public AlgorithmConfigurationBuilder() {
			
		}

		public AlgorithmConfigurationBuilder setGenerationAlgorithm(
				GenerationAlgorithm generationAlgorithm) {
			this.generationAlgorithm = generationAlgorithm;
			return this;
		}

		public AlgorithmConfigurationBuilder setInsertionAlgorithm(
				InsertionAlgorithm insertionAlgorithm) {
			this.insertionAlgorithm = insertionAlgorithm;
			return this;
		}

		public AlgorithmConfigurationBuilder setRemovalAlgorithm(
				RemovalAlgorithm removalAlgorithm) {
			this.removalAlgorithm = removalAlgorithm;
			return this;
		}

		public AlgorithmConfigurationBuilder setOptimizationAlgorithm(
				OptimizationAlgorithm optimizationAlgorithm) {
			this.optimizationAlgorithm = optimizationAlgorithm;
			return this;
		}

		public AlgorithmConfigurationBuilder setObjective(Objective objective) {
			this.objective = objective;
			return this;
		}

		public AlgorithmConfigurationBuilder setScheduler(Scheduler scheduler) {
			this.scheduler = scheduler;
			return this;
		}
		
		public AlgorithmConfiguration build() {
			return new AlgorithmConfiguration(
					generationAlgorithm,
					insertionAlgorithm,
					removalAlgorithm,
					optimizationAlgorithm,
					objective,
					scheduler);
		}
	}
	
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
	
	public static AlgorithmConfigurationBuilder createBuilder() {
		return new AlgorithmConfigurationBuilder();
	}
	
	public static AlgorithmConfiguration createDefault() {
		return new AlgorithmConfiguration(
				new SweepGeneration(), 
				new GreedyInsertion(), 
				new WorstRemoval(), 
				new TabuOptimization(), 
				new TotalDistanceObjective(), 
				new DriveFirstScheduler());
	}
}
