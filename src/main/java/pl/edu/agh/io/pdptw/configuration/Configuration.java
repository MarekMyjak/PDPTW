package pl.edu.agh.io.pdptw.configuration;

import lombok.Data;
import pl.edu.agh.io.pdptw.algorithm.generation.GenerationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;

@Data
public class Configuration {
	private String requestsPath;
	private String vehiclesPath;
	private String configPath;
	private GenerationAlgorithm generationAlgorithm;
	private InsertionAlgorithm insertionAlgorithm;
	private RemovalAlgorithm removalAlgorithm;
	private OptimizationAlgorithm optimizationAlgorithm;

}
