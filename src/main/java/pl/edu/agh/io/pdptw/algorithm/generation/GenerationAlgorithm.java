package pl.edu.agh.io.pdptw.algorithm.generation;

import java.util.Set;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;

public interface GenerationAlgorithm {
    public Solution generateSolution(Set<Request> requestPool);
}
