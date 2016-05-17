package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode

public final class OptimalRequestPosition {
	private static final int DEFAULT_VALUE = Integer.MAX_VALUE;
	
	private int pickupPosition;
	private int deliveryPosition;
	private double objectiveValue;
	
	public static OptimalRequestPosition createDefault() {
		return new OptimalRequestPosition(DEFAULT_VALUE, DEFAULT_VALUE, DEFAULT_VALUE);
	}
}
