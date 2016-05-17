package pl.edu.agh.io.pdptw.algorithm.removal;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.Pair;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface RemovalAlgorithm {
	
	public Pair<Integer, Integer> findBestRemovalPositions(Vehicle vehicle, Objective objective);
	public Request removeRequestForVehicle(Vehicle vehicle, Objective objective);
	public Request removeRequestFromSolution(Solution solution, Objective objective);
}
