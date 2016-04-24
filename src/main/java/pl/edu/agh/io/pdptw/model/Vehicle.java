package pl.edu.agh.io.pdptw.model;


public class Vehicle {
    private final Integer id;
    private final Integer maxCapacity;
    private Integer actualLoaded;
    private Location location;
    private final Location startLocation;
    private Route route;
    
	public Vehicle(Integer id, Integer maxCapacity, Integer actualLoaded,
			Location location, Location startLocation, Route route) {
		
		super();
		this.id = id;
		this.maxCapacity = maxCapacity;
		this.actualLoaded = actualLoaded;
		this.location = location;
		this.startLocation = startLocation;
		this.route = route;
	}

	public Integer getActualLoaded() {
		return actualLoaded;
	}

	public void setActualLoaded(Integer actualLoaded) {
		this.actualLoaded = actualLoaded;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public Integer getMaxCapacity() {
		return maxCapacity;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
	
}
