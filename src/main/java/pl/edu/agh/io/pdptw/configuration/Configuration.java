package pl.edu.agh.io.pdptw.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Configuration {
	private final String requestsPath;
	private final String vehiclesPath;
	private final String outputPath;
	private final boolean dynamic;
	private final AlgorithmConfiguration algorithms;
}
