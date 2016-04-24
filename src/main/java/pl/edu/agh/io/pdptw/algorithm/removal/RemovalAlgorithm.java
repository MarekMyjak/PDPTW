package pl.edu.agh.io.pdptw.algorithm.removal;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;

public interface RemovalAlgorithm {
	public Request removeRequest(Solution solution);
}
