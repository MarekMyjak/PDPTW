package pl.edu.agh.io.pdptw.configuration;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class AlgorithmsDescription {
	String generationAlgorithmName;
	String insertionAlgorithmName;
	String removalAlgorithmName;
	String optimizationAlgorithmName;
	String objectiveName;
}
