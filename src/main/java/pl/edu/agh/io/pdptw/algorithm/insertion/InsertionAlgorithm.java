package pl.edu.agh.io.pdptw.algorithm.insertion;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface InsertionAlgorithm {
	
	RequestPositions findBestInsertionPositions(
			PickupRequest pickup, Vehicle vehicle, Configuration configuration);
	boolean insertRequestForVehicle(PickupRequest pickup, Vehicle vehicle, Configuration configuration);
	boolean insertRequestToSolution(PickupRequest pickup, Solution solution,Configuration configuration);
}
