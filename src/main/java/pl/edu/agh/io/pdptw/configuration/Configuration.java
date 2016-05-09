package pl.edu.agh.io.pdptw.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Configuration {
	@Getter private final String requestsPath;
	@Getter private final String vehiclesPath;
	@Getter private final String outputPath;
	@Getter private final boolean dynamic;
	@Getter private final AlgorithmConfiguration algorithms;
}
