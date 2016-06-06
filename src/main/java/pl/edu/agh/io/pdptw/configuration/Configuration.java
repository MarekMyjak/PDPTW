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
}
