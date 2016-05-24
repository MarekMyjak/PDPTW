package pl.edu.agh.io.pdptw.model;

import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
	
	public List<Route> getRoutes() {
		return vehicles.stream()
				.map(Vehicle::getRoute)
				.collect(Collectors.toList());
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}
	
	public List<Request> getRequests() {
		return getRoutes().stream()
				.flatMap(r -> r.getRequests().stream())
				.collect(Collectors.toList());
	}
}
