package pl.edu.agh.io.pdptw.algorithm.insertion;

import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.model.OptimalRequestPosition;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class RegretInsertion implements InsertionAlgorithm {

	@Override
	public boolean insertRequestForVehicle(PickupRequest pickup, Vehicle vehicle,
			Objective objective) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public OptimalRequestPosition findBestInsertionPositions(
			PickupRequest pickup, Vehicle vehicle, Objective objective) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertRequestToSolution(PickupRequest pickup,
			Solution solution, Objective objective) {
		// TODO Auto-generated method stub
		return false;
	}


}
