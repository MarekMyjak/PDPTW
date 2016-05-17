package pl.edu.agh.io.pdptw.algorithm.objective;

import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public interface Objective {
	public double calculate(Solution solution);
	public double calculateForVehicle(Vehicle vehicle);
}
