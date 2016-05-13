package pl.edu.agh.io.pdptw.algorithm.generation;

import java.util.HashSet;
import java.util.Set;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class SweepGeneration implements GenerationAlgorithm {

	@Override
	public Solution generateSolution(Set<Request> requestPool) {
		Set<Vehicle> vehicles = new HashSet<>();
		Solution result = new Solution(vehicles);
		
		
		
		return result;
	}
}
