package pl.edu.agh.io.pdptw.model;

import lombok.ToString;

@ToString
public enum RequestType {
	PICKUP("pickup"),
	DELIVERY("delivery");
	
	private final String name;
	
	private RequestType(String name) {
		this.name = name;
	}
}
