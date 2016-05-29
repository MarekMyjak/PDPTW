package pl.edu.agh.io.pdptw.algorithm.optimization;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ToString
@EqualsAndHashCode

public class TabuList {
	private static final int DEFAULT_ITERATION_NO = 0;
	private List<Integer> tabuStatusExpirationTimes;
	private final int size;
	private final Objective objective;
	
	public TabuList(int size, Configuration config) {
		this.size = size;
		this.tabuStatusExpirationTimes = new ArrayList<>();
		IntStream.range(0, size).forEach(
				i -> tabuStatusExpirationTimes.add(DEFAULT_ITERATION_NO));
		this.objective = config.getAlgorithms().getObjective();
	}
	
	public boolean isForbidden(Solution solution, int iterationNo) {
		boolean isForbidden = true;
		int position = getPositionForObjective(objective.calculate(solution));
		
		if (iterationNo >= tabuStatusExpirationTimes.get(position)) {
			tabuStatusExpirationTimes.set(position, DEFAULT_ITERATION_NO);
			isForbidden = false;
		}
		
		return isForbidden;
	}
	
	public boolean isForbiddenByObjective(double objectiveValue, int iterationNo) {
		boolean isForbidden = true;
		int position = getPositionForObjective(objectiveValue);
		
		if (iterationNo >= tabuStatusExpirationTimes.get(position)) {
			tabuStatusExpirationTimes.set(position, DEFAULT_ITERATION_NO);
			isForbidden = false;
		}
		return isForbidden;
	}
	
	private int getPositionForObjective(double objectiveValue) {
		return ((int) (objectiveValue * 100.00)) % size;
	}
	
	public boolean setSolutionAsTabu(Solution solution, int expirationIterationNo) {
		boolean solutionNotFound = true;
		int position = getPositionForObjective(objective.calculate(solution));
		
		if (tabuStatusExpirationTimes.get(position).equals(DEFAULT_ITERATION_NO)) {
			tabuStatusExpirationTimes.set(position, expirationIterationNo);
		} else {
			solutionNotFound = false;
		}
		
		return solutionNotFound;
	}
	
	public boolean setAsTabuForObjective(double objectiveValue, int iterationNo) {
		boolean solutionNotFound = true;
		int position = getPositionForObjective(objectiveValue);
		
		if (tabuStatusExpirationTimes.get(position).equals(DEFAULT_ITERATION_NO)) {
			tabuStatusExpirationTimes.set(position, iterationNo);
		} else {
			solutionNotFound = false;
		}
		
		return solutionNotFound;
	}
}
