package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Getter;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

@Getter
public class AdaptiveMemory {
	private static final double MAX_OBJECTIVE_DIFFERENCE = 0.001;
	private List<Solution> solutions;
	private final int SIZE;
	private final Objective objective;
	private final InsertionAlgorithm insertionAlg;
	
	public AdaptiveMemory(int size, Configuration configuration) {
		this.SIZE = size;
		this.solutions = new ArrayList<>(SIZE);
		this.objective = configuration.getAlgorithms().getObjective();
		this.insertionAlg = configuration.getAlgorithms().getInsertionAlgorithm();
	}
	
	/* a simplified version of checking whether
	 * the solution has already been added to the 
	 * solutions list */
	
	public boolean contains(Solution solution) {
		boolean sameSolutionsFound = false;
		Iterator<Solution> solutionsIt = solutions.iterator();
		
		while (solutionsIt.hasNext() && !sameSolutionsFound) {
			
			Solution s = solutionsIt.next();
			
			if (Math.abs(
					solution.getObjectiveValue() - s.getObjectiveValue())
				<= MAX_OBJECTIVE_DIFFERENCE) {
				
				sameSolutionsFound = true;
			}
		}
		
		return sameSolutionsFound;
	}
	
	public synchronized boolean addSolution(Solution solution) {
		boolean success = true;
		
		if (solutions.size() >= SIZE
				|| contains(solution)) {
			success = false;
		} else {
			
			/* we need to copy the solution
			 * to prevent its unintended modifications */
			
			Solution copy = solution.copy();
			Iterator<Solution> it = solutions.iterator();
			int position = 0;
			while (it.hasNext() 
					&& it.next().getObjectiveValue() < copy.getObjectiveValue()) {
				position++;
			}
			
			if (position < SIZE) {
				solutions.add(position, copy);
			}
		}
		
		return success;
	}
	
	public boolean addSolutionWithObjective(Solution solution, double objectiveValue) {
		boolean success = true;
		
		if (solutions.size() >= SIZE
				|| contains(solution)) {
			success = false;
		} else {
			solutions.add(solution);
		}
		
		return success;
	}
	
	public boolean removeSolution(Solution solution) {
		boolean present = contains(solution);
		
		if (present) {
			solutions.remove(solution);
		}
		
		return present;
	}
	
	public Solution removeSolutionOnPosition(int position) {
		return solutions.remove(position);
	}

	/* the general idea behind this method
	 * is to crate new solution from randomly picked
	 * routes chosen from random solutions 
	 * stored in the adaptive memory
	 * 
	 * we assume that solutions with lesser
	 * objective values should be picked more
	 * frequently so we first sort the solutions
	 * by the objective value (ascending) and
	 * choose the sector which we'll draw 
	 * specific route from
	 * 
	 * example: 
	 * 
	 * treshold = 0.7, two iterations
	 * 
	 * 1. drawn random value 0.55 <= 0.7 ? OK
	 * move the sector end index up
	 * 
	 * 0 <- sector start
	 * 1
	 * 2
	 * 3
	 * 4 <- sector end
	 * 5
	 * 6
	 * 7
	 * 8
	 * 9
	 * 
	 * 2. drawn random value 0.81 <= 0.7 ? NO
	 * move the sector start index down
	 * 
	 * 0 
	 * 1
	 * 2 <- sector start
	 * 3
	 * 4 <- sector end
	 * 
	 * etc.
	 * 
	 * note that we use auxilliary data structures to
	 * keep track on which vehicles have already been
	 * used and which routes share requests
	 * with those already added
	 * 
	 * solutionIndices: List - inidces of the solutions
	 * in the sortedSolutions list
	 * 
	 * vehicleIndicesForSoulution: Map
	 * key: solution index in the sortedSolutions list
	 * value: a list of indices of vehicles for the 
	 * corresponding solution
	 * 
	 * requestIndices: List
	 * stores indices of the requests left to insert
	 * note that values of the indices depend on the
	 * order of the mentioned requests in the first
	 * solution on the sortedSolutions list
	 * 
	 * example:
	 * 
	 * sortedSolutions: [s1, s2, s3]
	 * solutionIndices: [ 0,  1,  2]
	 * vehicleIndicesForSoulution: 
	 * 0 : [ 0,  1,  2]
	 * s1: [v1, v2, v3]
	 * 
	 * 1 : [ 0,  1]
	 * s2: [v4, v5]
	 * 
	 * 2 : [ 0,   1]
	 * s3: [v9, v11]
	 * 
	 * requestIndices: [ 0, 1,   2, 3, 4, 5,   6, 7]  (indices)
	 *             s1: [[1, 2], [3, 5, 4, 6], [7, 8]] (requests ids)
	 *                   v1      v2            v3	  (vehicles)
	 * */
	
	public Solution createRandomSolution(double threshold, int iterationsNo) 
			throws IllegalArgumentException {
		
		if (threshold < 0 || threshold >= 1) {
			throw new IllegalArgumentException("Invalid threshold value."
					+ " Should belong to the range [0, 1)");
		}
		if (iterationsNo < 0) {
			throw new IllegalArgumentException("Invalid iterations number."
					+ " Should be non-negative");
		}

		Solution newSolution = null;
		List<Solution> sortedSolutions = solutions.stream()
				.sorted((s1, s2) -> Double.compare(
						s1.getObjectiveValue(),
						s2.getObjectiveValue()))
				.collect(Collectors.toList());
		
		List<Integer> solutionIndices = new ArrayList<>(sortedSolutions.size());
		Map<Integer, List<Integer>> routeIndicesForSolution = new HashMap<>();
		IntStream.range(0, sortedSolutions.size()).forEach(i -> solutionIndices.add(i));
		
		for (Integer i : solutionIndices) {
			
			/* .boxed() transforms the IntStream to a Stream<Integer>
			 * which can be collected in the form 
			 * of a list */
			
			List<Integer> routeIds = IntStream
					.range(0, sortedSolutions
							.get(i)
							.getRoutes()
							.size())
					.boxed()
					.collect(Collectors.toList());
			
			routeIndicesForSolution.put(i, routeIds);
		}
		
		if (sortedSolutions.size() > 0) {
			
			/* create shallow copy of all requests */
			
			Solution firstSolution = sortedSolutions.get(0).copy();
			List<Integer> requestsIds = firstSolution
					.getRequests()
					.stream()
					.map(r -> r.getId())
					.collect(Collectors.toList());
			List<Vehicle> pickedVehicles = new LinkedList<>();
			Set<Integer> pickedRequestsIds = new HashSet<>();
			
			while (requestsIds.size() > 0 && solutionIndices.size() > 0) {
				int sectorStart = 0;
				int sectorEnd = solutionIndices.size() - 1;
				
				for (int i = 0; i < iterationsNo; i++) {
					if (Math.random() <= threshold) {
						sectorEnd -= (sectorEnd - sectorStart) / 2; 
					} else {
						sectorStart += (sectorEnd - sectorStart) / 2; 
					}
				}
				
				/* draw a random solution from the chosen sector 
				 * and a random route from the mentioned solution */
				
				int diff = sectorEnd - sectorStart;
				int solutionIndex = (int) (sectorStart + Math.random() * diff);
				Solution pickedSolution = sortedSolutions.get(
						solutionIndices.get((int) solutionIndex));
				List<Integer> routeIndices = 
						routeIndicesForSolution.get(solutionIndices.get(solutionIndex));
				int routeIndex = ListUtils.getRandomElement(routeIndices);
				
				Vehicle pickedVehicle = pickedSolution.getVehicles()
						.get(routeIndex);
				Route pickedRoute = pickedVehicle.getRoute();
				
				boolean uniqueRequests = true;
				Iterator<Request> requestsIt = 
						pickedRoute.getRequests().iterator();

				/* check if the selected route
				 * has any requests shared with
				 * the already added routes */
				
				while (requestsIt.hasNext() && uniqueRequests) {
					uniqueRequests = !pickedRequestsIds.contains(requestsIt.next().getId());
				}
				
				if (uniqueRequests) {
					
					/* note that we should create a copy of
					 * the picked vehicle
					 * in other case we could damage the
					 * original ones during insertion
					 * of the left requests */
					
					Vehicle vehicleCopy = pickedVehicle.copy();
					pickedVehicles.add(vehicleCopy);
					pickedRequestsIds.addAll(pickedVehicle.getRoute().getRequests()
							.stream()
							.map(r -> r.getId())
							.collect(Collectors.toList()));
							
					requestsIds.removeAll(
							pickedVehicle.getRoute()
								.getRequests()
								.stream()
								.map(r -> r.getId())
								.collect(Collectors.toList()));
				}
				
				/* WARNING! 
				 * 
				 * we're removing here an Integer value,
				 * not the element at the position
				 * routeIndex 
				 */
				
				routeIndices.remove(Integer.valueOf(routeIndex));
				
				if (routeIndices.size() == 0) {
					
					/* we're removing here the Integer element
					 * not the element at the position solutionIndex 
					 * 
					 * note the order of removing solutions:
					 * we're updating the map first and then 
					 * the indices list
					 * */
					
					routeIndicesForSolution.remove(Integer.valueOf(solutionIndices.get(solutionIndex)));
					solutionIndices.remove(Integer.valueOf(solutionIndices.get(solutionIndex)));
				}
			}
			
			newSolution = new Solution(pickedVehicles);
			
			if (requestsIds.size() > 0) {
				Iterator<PickupRequest> leftRequestsIt = firstSolution.getRequests().stream()
						.filter(r -> r.getType() == RequestType.PICKUP)
						.map(r -> (PickupRequest) r)
						.filter(r -> requestsIds.contains(r.getId()))
						.collect(Collectors.toList())
						.iterator();
				
				boolean insertedSuccessfully = true;
				while (leftRequestsIt.hasNext() && insertedSuccessfully) {
					PickupRequest pickup = leftRequestsIt.next();
					insertedSuccessfully = insertionAlg.insertRequestToSolution(pickup, newSolution, objective);
				}
				
				/* if inserting all requests is not possible
				 * return the best current solution */
				
				if (!insertedSuccessfully) {
					newSolution = sortedSolutions.get(0);
				}
			}
		}
		
		return newSolution;
	}

	/* while the solutions list is bigger than SIZE
	 * keep removing the last elements */
	
	public void update() {
		while (solutions.size() > SIZE) {
			solutions.remove(solutions.size() - 1);
		}
	}
}
