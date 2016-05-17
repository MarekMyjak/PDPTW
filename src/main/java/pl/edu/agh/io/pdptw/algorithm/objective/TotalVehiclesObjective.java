package pl.edu.agh.io.pdptw.algorithm.objective;

import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class TotalVehiclesObjective implements Objective {

	@Override
	public double calculate(Solution solution) {
		double result = solution.getVehicles().stream()
			.filter(v -> {
				Route route = v.getRoute();
				return (route.getRequests().size()
						+ route.getServicedRequests().size()) > 0;
			})
			.collect(Collectors.toList())
			.size();

		return result;
	}

	@Override
	public double calculateForVehicle(Vehicle vehicle) {
		return 1;
	}

}
