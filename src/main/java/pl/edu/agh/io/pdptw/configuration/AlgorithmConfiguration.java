package pl.edu.agh.io.pdptw.configuration;

import com.google.inject.Inject;

import lombok.Value;
import pl.edu.agh.io.pdptw.algorithm.decomposition.DecompositionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.decomposition.SweepDecomposition;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
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

@Value
public class AlgorithmConfiguration {
	GenerationAlgorithm generationAlgorithm;
	InsertionAlgorithm insertionAlgorithm;
	RemovalAlgorithm removalAlgorithm;
	OptimizationAlgorithm optimizationAlgorithm;
	Objective objective;
	Scheduler scheduler;
	DecompositionAlgorithm decompositionAlgorithm;
	
	public static class AlgorithmConfigurationBuilder {
		private GenerationAlgorithm generationAlgorithm = new SweepGeneration();
		private InsertionAlgorithm insertionAlgorithm = new GreedyInsertion();
		private RemovalAlgorithm removalAlgorithm = new WorstRemoval();
		private OptimizationAlgorithm optimizationAlgorithm = new TabuOptimization();
		private Objective objective = new TotalDistanceObjective();
		private Scheduler scheduler = new DriveFirstScheduler();
		private DecompositionAlgorithm decompositionAlgorithm = new SweepDecomposition();
		
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
		
		public AlgorithmConfigurationBuilder setDecompositionAlgorithm(DecompositionAlgorithm decomposition) {
			this.decompositionAlgorithm = decomposition;
			return this;
		}
		
		public AlgorithmConfiguration build() {
			return new AlgorithmConfiguration(
					generationAlgorithm,
					insertionAlgorithm,
					removalAlgorithm,
					optimizationAlgorithm,
					objective,
					scheduler,
					decompositionAlgorithm);
		}
	}
	
	@Inject
	public AlgorithmConfiguration(GenerationAlgorithm generationAlgorithm,
			InsertionAlgorithm insertionAlgorithm,
			RemovalAlgorithm removalAlgorithm,
			OptimizationAlgorithm optimizationAlgorithm,
			Objective objective,
			Scheduler scheduler,
			DecompositionAlgorithm decomposition) {
		
		this.generationAlgorithm = generationAlgorithm;
		this.insertionAlgorithm = insertionAlgorithm;
		this.removalAlgorithm = removalAlgorithm;
		this.optimizationAlgorithm = optimizationAlgorithm;
		this.objective = objective;
		this.scheduler = scheduler;
		this.decompositionAlgorithm = decomposition;
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
				new DriveFirstScheduler(),
				new SweepDecomposition());
	}
}
