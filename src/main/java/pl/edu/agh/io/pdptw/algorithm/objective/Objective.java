package pl.edu.agh.io.pdptw.algorithm.objective;

import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface Objective {
	double calculate(Solution solution);
	double calculateForVehicle(Vehicle vehicle);
}
