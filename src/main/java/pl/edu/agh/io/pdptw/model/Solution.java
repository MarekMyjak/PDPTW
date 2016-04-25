package pl.edu.agh.io.pdptw.model;

import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
	private final Set<Vehicle> vehicles;
	
	public Solution(Set<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
	public Set<Route> getRoutes() {
		return vehicles.stream()
				.map(Vehicle::getRoute)
				.collect(Collectors.toSet());
	}

	public Set<Vehicle> getVehicles() {
		return vehicles;
	}
}
