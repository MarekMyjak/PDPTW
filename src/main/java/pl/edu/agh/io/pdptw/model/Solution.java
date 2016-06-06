package pl.edu.agh.io.pdptw.model;

import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;

@Getter
@ToString
@EqualsAndHashCode

public class Solution {
	private final List<Vehicle> vehicles;
	@Setter private double objectiveValue;
	
	public Solution(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
		this.objectiveValue= 0.0;
	}
	
	public void updateOjectiveValue(Objective objective) {
		this.objectiveValue = objective.calculate(this);
	}
	
	public List<Route> getRoutes() {
		return vehicles.stream()
				.map(Vehicle::getRoute)
				.collect(Collectors.toList());
	}

	public List<Request> getRequests() {
		return getRoutes().stream()
				.flatMap(r -> r.getRequests().stream())
				.collect(Collectors.toList());
	}
	
	public Solution copy() {
		List<Vehicle> vehiclesCopies = vehicles.stream()
				.map(v -> v.copy())
				.collect(Collectors.toList());
		return new Solution(vehiclesCopies);
	}
}
