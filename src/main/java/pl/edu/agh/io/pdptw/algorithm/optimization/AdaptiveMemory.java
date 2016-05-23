package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class AdaptiveMemory {
	private static final double MAX_OBJECTIVE_DIFFERENCE = 0.001;
	private List<Solution> solutions;
	private final int SIZE;
	private final Objective objective;
	private final InsertionAlgorithm insertionAlg;
	
	public AdaptiveMemory(int size, Configuration config) {
		this.SIZE = size;
		this.solutions = new ArrayList<>(SIZE);
		this.objective = config.getAlgorithms().getObjective();
		this.insertionAlg = config.getAlgorithms().getInsertionAlgorithm();
	}
	
	/* this method is responsible for telling
	 * if there is in the solutions list a solution
	 * that is made of the same set of routes
	 * we need to consider different permutations
	 * of the routes on the routes list 
	 * of the solution
	 * e.g.
	 * solution made of three routes
	 * s1 = [r1, r2, r3]
	 * s2 = [r3, r1, r2]
	 * 
	 * solutions = [s1]
	 * 
	 * we assume that AdaptiveMemory.contains(s2)
	 * should return true - order of the routes 
	 * isn't important
	 *  */
	
	public boolean contains(Solution solution) {
		boolean sameSolutionsFound = false;
		Iterator<Solution> solutionsIt = solutions.iterator();
		
		while (solutionsIt.hasNext() && !sameSolutionsFound) {
			
			Solution s = solutionsIt.next();
			
			/* if objective values for the solutions
			 * are unequal they must be different;
			 * if they're equal we need to check each route */
			
			if (Math.abs(
					objective.calculate(solution) 
					- objective.calculate(s))
				<= MAX_OBJECTIVE_DIFFERENCE) {
				
				boolean sameRoutes = true;
				Iterator<Route> routesIt = solution.getRoutes().iterator();
				
				while (routesIt.hasNext() && sameRoutes) {
					Route r = routesIt.next();
					Request firstRequest = r.getRequests().get(0);
					Optional<Route> foundRoute = s.getRoutes()
							.stream()
							.filter(route -> route.getRequests().get(0).equals(firstRequest))
							.findFirst();
							
					if (foundRoute.isPresent() 
							&& foundRoute.get().getRequests().size()
								== r.getRequests().size()) {
						
						/* first let's checl the instance 
						 * equality (identity) 
						 * if the routes are not
						 * identical let's check
						 * their requests */
						
						if (r != foundRoute.get()) {
							Iterator<Request> foundIt = foundRoute.get()
									.getRequests()
									.iterator();
							Iterator<Request> testedIt = r.getRequests().iterator();
							boolean sameRequests = true;
							
							while (testedIt.hasNext()
									&& foundIt.hasNext()
									&& sameRequests) {
								
								sameRequests = testedIt.next().equals(foundIt.next());
							}
							
							/* at this moment we are
							 * sure that these two routes
							 * are not same */
							
							sameRoutes = sameRequests;
						}
						
					} else {
						sameRoutes = false;
					}
				/* check_same_routes_loop */
				} 
				
				sameSolutionsFound = sameRoutes;
				
			/* same objective value */
				
			} else {
				sameSolutionsFound = false;
			}
		}
		
		return sameSolutionsFound;
	}
	
	public boolean containsWithObjective(Solution solution, double objectiveValue) {
		boolean sameSolutionsFound = false;
		Iterator<Solution> solutionsIt = solutions.iterator();
		
		while (solutionsIt.hasNext() && !sameSolutionsFound) {
			
			Solution s = solutionsIt.next();
			
			/* if objective values for the solutions
			 * are unequal they must be different;
			 * if they're equal we need to check each route */
			
			if (Math.abs(
					objectiveValue 
					- objective.calculate(s))
				<= MAX_OBJECTIVE_DIFFERENCE) {
				
				boolean sameRoutes = true;
				Iterator<Route> routesIt = solution.getRoutes().iterator();
				
				while (routesIt.hasNext() && sameRoutes) {
					Route r = routesIt.next();
					Request firstRequest = r.getRequests().get(0);
					Optional<Route> foundRoute = s.getRoutes()
							.stream()
							.filter(route -> route.getRequests().get(0).equals(firstRequest))
							.findFirst();
							
					if (foundRoute.isPresent() 
							&& foundRoute.get().getRequests().size()
								== r.getRequests().size()) {
						
						/* first let's checl the instance 
						 * equality (identity) 
						 * if the routes are not
						 * identical let's check
						 * their requests */
						
						if (r != foundRoute.get()) {
							Iterator<Request> foundIt = foundRoute.get()
									.getRequests()
									.iterator();
							Iterator<Request> testedIt = r.getRequests().iterator();
							boolean sameRequests = true;
							
							while (testedIt.hasNext()
									&& foundIt.hasNext()
									&& sameRequests) {
								
								sameRequests = testedIt.next().equals(foundIt.next());
							}
							
							/* at this moment we are
							 * sure that these two routes
							 * are not same */
							
							sameRoutes = sameRequests;
						}
						
					} else {
						sameRoutes = false;
					}
				/* check_same_routes_loop */
				} 
				
				sameSolutionsFound = sameRoutes;
				
			/* same objective value */
				
			} else {
				sameSolutionsFound = false;
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
			solutions.add(solution);
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
	 *  treshold = 0.7, two iterations
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
	 * */
	
	public Solution createRandomSolution(double treshold, int iterationsNo) 
			throws IllegalArgumentException {
		
		if (treshold < 0 || treshold >= 1) {
			throw new IllegalArgumentException("Invalid treshold value."
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
		Map<Integer, List<Integer>> routeIndicesForSoulution = new HashMap<>();
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
			
			routeIndicesForSoulution.put(i, routeIds);
		}
		
		if (solutions.size() > 0) {
			
			/* create shallow copy of all requests */
			
			Solution firstSolution = sortedSolutions.get(0);
			List<Integer> requestIndices = firstSolution
					.getRequests()
					.stream()
					.map(r -> r.getId())
					.collect(Collectors.toList());
			List<Vehicle> pickedVehicles = new LinkedList<>();
			List<Route> pickedRoutes = new LinkedList<>();
			
			while (requestIndices.size() > 0 && solutionIndices.size() > 0) {
				int sectorStart = 0;
				int sectorEnd = solutionIndices.size() - 1;
				
				for (int i = 0; i < iterationsNo; i++) {
					if (Math.random() <= treshold) {
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
						solutionIndices.get(solutionIndex));
				
				List<Integer> routeIndices = 
						routeIndicesForSoulution.get(solutionIndex);
				int routeIndex = (int) (Math.random() 
						* routeIndices.size());
				
				Vehicle pickedVehicle = pickedSolution.getVehicles()
						.get(routeIndices.get(routeIndex));
				Route pickedRoute = pickedVehicle.getRoute();
				
				boolean uniqueRequests = true;
				Iterator<Request> requestsIt = 
						pickedRoute.getRequests().iterator();

				/* check if the selected route
				 * has any requests shared with
				 * the already added routes */
				
				while (requestsIt.hasNext() && uniqueRequests) {
					Iterator<Route> routesIt = pickedRoutes.iterator();
					Request curRequest = requestsIt.next();
					
					while (routesIt.hasNext() && uniqueRequests) {
						uniqueRequests = !routesIt.next()
								.getRequests()
								.contains(curRequest);
					}
				}
				
				if (uniqueRequests) {
					pickedVehicles.add(pickedVehicle);
					requestIndices.removeAll(
							pickedVehicle.getRoute()
								.getRequests()
								.stream()
								.map(r -> r.getId())
								.collect(Collectors.toList()));
				}
				
				/* WARNING! 
				 * 
				 * we're removing here Integer value
				 * based on the passed index in the list!
				 * e.g. if routeIndex == 1
				 * and routeIndices looks like [2, 4, 9, 1]
				 * we'll remove the second element [4]!
				 * so after this operation routeIndices
				 * will be equal to [2, 9, 1]
				 * 
				 * route index is a primitive int value
				 * and since the list provides a 
				 * List.remove(__int__ index) method
				 * this method will be used and 
				 * not the List.remove(Object)
				 * (List.remove(Integer)) */
				
				routeIndices.remove(routeIndex);
				
				/* if we've used the last route
				 * let's discard this solution 
				 * from the generation process */
				
				if (routeIndices.size() == 0) {
					
					/* same situation as above */
					
					solutionIndices.remove(solutionIndex);
					routeIndicesForSoulution.remove(solutionIndex);
				}
			}
			
			newSolution = new Solution(pickedVehicles);
			
			if (requestIndices.size() > 0) {
				List<PickupRequest> leftRequests = requestIndices.stream()
						.map(i -> firstSolution.getRequests().get(i))
						.filter(r -> r.getType() == RequestType.PICKUP)
						.map(r -> (PickupRequest) r)
						.collect(Collectors.toList());
				
				for (PickupRequest pickup : leftRequests) {
					insertionAlg.insertRequestToSolution(pickup, newSolution, objective);
				}
			}
		}
		
		return newSolution;
	}
}
