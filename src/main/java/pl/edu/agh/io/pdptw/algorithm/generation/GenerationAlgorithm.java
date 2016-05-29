package pl.edu.agh.io.pdptw.algorithm.generation;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

import java.util.List;

public interface GenerationAlgorithm {
    Solution generateSolution(List<Request> requestPool, List<Vehicle> vehicles,
                              Configuration configuration) throws IllegalArgumentException;
}
