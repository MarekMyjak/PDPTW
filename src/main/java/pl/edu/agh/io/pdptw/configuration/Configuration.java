package pl.edu.agh.io.pdptw.configuration;

import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;

public class Configuration {
	private String requestsPath;
	private String vehiclesPath;
	private String configPath;
	private GenerationAlgorithm generationAlgorithm;
	private InsertionAlgorithm insertionAlgorithm;
	private RemovalAlgorithm removalAlgorithm;
	private OptimizationAlgorithm optimizationAlgorithm;
	
	public Configuration() {
		
	}

	public String getRequestsPath() {
		return requestsPath;
	}

	public Configuration setRequestsPath(String requestsPath) {
		this.requestsPath = requestsPath;
		return this;
	}

	public String getVehiclesPath() {
		return vehiclesPath;
	}

	public Configuration setVehiclesPath(String vehiclesPath) {
		this.vehiclesPath = vehiclesPath;
		return this;
	}

	public String getConfigPath() {
		return configPath;
	}

	public Configuration setConfigPath(String configPath) {
		this.configPath = configPath;
		return this;
	}

	public GenerationAlgorithm getGenerationAlgorithm() {
		return generationAlgorithm;
	}

	public Configuration setGenerationAlgorithm(GenerationAlgorithm generationAlgorithm) {
		this.generationAlgorithm = generationAlgorithm;
		return this;
	}

	public InsertionAlgorithm getInsertionAlgorithm() {
		return insertionAlgorithm;
	}

	public Configuration setInsertionAlgorithm(InsertionAlgorithm insertionAlgorithm) {
		this.insertionAlgorithm = insertionAlgorithm;
		return this;
	}

	public RemovalAlgorithm getRemovalAlgorithm() {
		return removalAlgorithm;
	}

	public Configuration setRemovalAlgorithm(RemovalAlgorithm removalAlgorithm) {
		this.removalAlgorithm = removalAlgorithm;
		return this;
	}

	public OptimizationAlgorithm getOptimizationAlgorithm() {
		return optimizationAlgorithm;
	}

	public Configuration setOptimizationAlgorithm(OptimizationAlgorithm optimizationAlgorithm) {
		this.optimizationAlgorithm = optimizationAlgorithm;
		return this;
	}
}
