package pl.edu.agh.io.pdptw.algorithm.insertion;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface InsertionAlgorithm {
	public int insertRequestToVehicleRoute(PickupRequest pickup, Vehicle vehicle, Objective objective);
}
