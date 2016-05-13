package pl.edu.agh.io.pdptw.algorithm.generation;

import java.util.List;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface GenerationAlgorithm {
    public Solution generateSolution(List<Request> requestPool, List<Vehicle> vehicles,
			InsertionAlgorithm insertionAlg, Objective objective) throws IllegalArgumentException;
}
