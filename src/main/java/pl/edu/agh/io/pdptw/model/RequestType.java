package pl.edu.agh.io.pdptw.model;

public enum RequestType {
	PICKUP("pickup"),
	DELIVERY("delivery");
	
	private final String name;
	
	private RequestType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
