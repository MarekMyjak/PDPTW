package pl.edu.agh.io.pdptw.algorithm.objective;

import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

import java.util.stream.Collectors;

public class TotalVehiclesObjective implements Objective {

	@Override
	public double calculate(Solution solution) {

		return (double) solution.getVehicles().stream()
			.filter(v -> {
				Route route = v.getRoute();
				return (route.getRequests().size()
						+ route.getServicedRequests().size()) > 0;
			})
			.collect(Collectors.toList())
			.size();
	}

	@Override
	public double calculateForVehicle(Vehicle vehicle) {
		return 1;
	}

}
