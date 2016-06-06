package pl.edu.agh.io.pdptw.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.edu.agh.io.pdptw.model.Location;

@Data
@AllArgsConstructor

public class Configuration {
	private String requestsPath;
	private String vehiclesPath;
	private String outputPath;
	private boolean dynamic;
	private int iterations;
	private int decompositionCycles;
	private int iterationsPerDecomposition;
	private int maxVehiclesInGroup;
	private Location warehouseLocation;
	private AlgorithmConfiguration algorithms;
	
	@Override
	public String toString() {
		return "requestsPath: " + requestsPath
				+ "\r\nvehiclesPath: " + vehiclesPath
				+ "\r\noutputPath: " + outputPath
				+ "\r\ndynamic: " + isDynamic()
				+ "\r\niterations: " + iterations
				+ "\r\ndecompositionCycles: " + decompositionCycles
				+ "\r\niterationsPerDecomposition: " + iterationsPerDecomposition
				+ "\r\nmaxVehiclesInGroup: " + maxVehiclesInGroup
				+ "\r\nalgorithms:" 
					+ "\r\n\tgeneration: " + algorithms.getGenerationAlgorithm().getClass().getSimpleName()  
					+ "\r\n\tinsertion: " + algorithms.getInsertionAlgorithm().getClass().getSimpleName() 
					+ "\r\n\tremoval: " + algorithms.getRemovalAlgorithm().getClass().getSimpleName() 
					+ "\r\n\toptimization: " + algorithms.getOptimizationAlgorithm().getClass().getSimpleName() 
					+ "\r\n\tdecomposition: " + algorithms.getDecompositionAlgorithm().getClass().getSimpleName() 
					+ "\r\n\tobjective: " + algorithms.getObjective().getClass().getSimpleName() 
					+ "\r\n\tscheduler: " + algorithms.getScheduler().getClass().getSimpleName();
	}
}
