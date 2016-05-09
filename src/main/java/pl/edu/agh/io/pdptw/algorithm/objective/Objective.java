package pl.edu.agh.io.pdptw.algorithm.objective;

import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;

public interface Objective {
	public double calculate(Solution solution);
	public double calculateForRoute(Route route);
}
