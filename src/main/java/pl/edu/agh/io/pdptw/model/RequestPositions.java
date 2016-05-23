package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode

public final class RequestPositions {
	private static final int DEFAULT_VALUE = Integer.MAX_VALUE;
	
	private int pickupPosition;
	private int deliveryPosition;
	private double objectiveValue;
	
	public RequestPositions (int pickupPosition, int deliveryPosition) {
		this(pickupPosition, deliveryPosition, DEFAULT_VALUE);
	}
	
	public static RequestPositions createDefault() {
		return new RequestPositions(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE);
	}
}
