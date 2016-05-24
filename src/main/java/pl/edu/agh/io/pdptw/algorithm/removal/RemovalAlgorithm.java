package pl.edu.agh.io.pdptw.algorithm.removal;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface RemovalAlgorithm {
	
	RequestPositions findBestRemovalPositions(Vehicle vehicle, Configuration configuration);
	Request removeRequestForVehicle(Vehicle vehicle, Configuration configuration);
	Request removeRequestFromSolution(Solution solution, Configuration configuration);
}
