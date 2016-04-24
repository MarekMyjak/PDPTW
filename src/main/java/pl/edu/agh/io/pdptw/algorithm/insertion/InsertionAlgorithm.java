package pl.edu.agh.io.pdptw.algorithm.insertion;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;

public interface InsertionAlgorithm {
	public int insertRequest(Request request, Solution solution);
}
