package pl.edu.agh.io.pdptw.algorithm.decomposition;

import java.util.List;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;

public interface DecompositionAlgorithm {
	public List<Solution> decompose(Solution solution, Configuration configuration);
}
