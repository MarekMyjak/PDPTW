package pl.edu.agh.io.pdptw.algorithm.insertion;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.Pair;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface InsertionAlgorithm {
	
	public RequestPositions findBestInsertionPositions(
			PickupRequest pickup, Vehicle vehicle, Objective objective);
	public boolean insertRequestForVehicle(PickupRequest pickup, Vehicle vehicle, Objective objective);
	public boolean insertRequestToSolution(PickupRequest pickup, Solution solution, Objective objective);
}
