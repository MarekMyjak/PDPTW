package pl.edu.agh.io.pdptw.configuration;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class AlgorithmDescription {
	String generationAlgorithmName;
	String insertionAlgorithmName;
	String removalAlgorithmName;
	String optimizationAlgorithmName;
	String objectiveName;
	String schedulerName;
	String decompositionAlgorithmName;
}
