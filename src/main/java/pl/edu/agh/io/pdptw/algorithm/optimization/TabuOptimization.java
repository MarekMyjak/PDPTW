package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;
import pl.edu.agh.io.pdptw.configuration.AlgorithmConfiguration;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class TabuOptimization implements OptimizationAlgorithm {
	private static final int MAX_ITERATIONS = 5000;
	
	@Override
	public Solution optimize(Solution solution, Configuration configuration) {
		AlgorithmConfiguration algs = configuration.getAlgorithms();
		solution.setObjectiveValue(algs.getObjective().calculate(solution));
		Solution curSolution = solution;
		Solution bestSolution = solution;
		TabuList tabu = new TabuList(1000, configuration);
		AdaptiveMemory adaptiveMemory = new AdaptiveMemory(1000, configuration);
		adaptiveMemory.addSolution(bestSolution);
		
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			if (i % 250 == 0 && i != 0) {
				curSolution = adaptiveMemory.createRandomSolution(0.7, 3);
			}
			
			/* generate 5 nieighbors created using 
			 * ejection chains of maximum length 10 */
			
			final int iterationNo = i;
			Optional<Solution> bestNeighbor = generateNeighbors(curSolution, 5, 10, configuration)
					.stream()
					.filter(n -> !tabu.isForbiddenByObjective(n, iterationNo))
					.sorted((n1, n2) -> 
						(n1.getObjectiveValue() < n2.getObjectiveValue()) ? -1 : 1)
					.findFirst();
			
			if (bestNeighbor.isPresent()) {
				curSolution = bestNeighbor.get();
				tabu.setSolutionAsTabu(bestNeighbor.get(), i + 100);
				adaptiveMemory.addSolution(bestNeighbor.get());
				
				if (bestNeighbor.get().getObjectiveValue() 
						< bestSolution.getObjectiveValue()) {
					bestSolution = bestNeighbor.get();	
					LoggingUtils.info("New best solution found: " + bestSolution.getObjectiveValue());
				}
			}
			
			tabu.update(i);
			adaptiveMemory.update();
		}
		
		LoggingUtils.info("Optimization finished. Best found solution: " 
				+ bestSolution.getObjectiveValue());
		LoggingUtils.info("Initial number of requests: " + solution.getRequests().size());
		LoggingUtils.info("Number of requests: " + bestSolution.getRequests().size());
		
		return bestSolution;
	}
	
	/* neighborhood is using so called ejection chains - 
	 * we remove a request from one route and insert it to 
	 * another one in the most feasible place
	 * we repeat this process as long as we want
	 * 
	 *  example 
	 *  r1 = [p1, d1, p2, d2]
	 *  r2 = [p5, p7, d7, d5]
	 *  
	 *  new solution
	 *  
	 *  r1 = [p1, d1, __p7__, p1, d2, __d7__]
	 *  r2 = [p5, d5]
	 *  etc.
	 *  */
	
	public static List<Solution> generateNeighbors(Solution solution, int n, int maxChainLength, Configuration configuration) {
		List<Solution> neighbors = new LinkedList<>();
				
		InsertionAlgorithm insertion = configuration.getAlgorithms().getInsertionAlgorithm();
		RemovalAlgorithm removal = configuration.getAlgorithms().getRemovalAlgorithm();
		Objective objective = configuration.getAlgorithms().getObjective();
		
		/* generate n new solutions based on the passed one */
		for (int i = 0; i < n; i++) {
			
			/* create a shallow copy of each vehicle
			 * note that the route's requests list
			 * has to be set to a new list instation 
			 * so that we can safely modify it 
			 * without damaging the original route's list */
			
			List<Vehicle> vehicles = solution.getVehicles()
					.stream()
					.map(v -> {
						Vehicle copy = v.createShallowCopy();
						copy.setRoute(new Route(
								new LinkedList<>(v.getRoute().getRequests())));
						return copy;
					}).collect(Collectors.toList());
			
			Solution neighbor = new Solution(vehicles);
			Vehicle prevVehicle = ListUtils.getRandomElement(vehicles);
			Request prevEjected = removal.removeRequestForVehicle(prevVehicle, configuration);
			Request curEjected = null;
			PickupRequest pickupToInsert = null;
			boolean insertedSuccessfully = true;
			
			for (int j = 0; j < maxChainLength; j++) {
				Vehicle curVehicle = ListUtils.getRandomElement(vehicles);
				
				if (curVehicle.getRoute().getRequests().size() > 0) {
					Route routeCopy = new Route(new ArrayList<>(curVehicle.getRoute().getRequests()));
					curEjected = removal.removeRequestForVehicle(curVehicle, configuration);
					pickupToInsert = (PickupRequest) 
							((prevEjected.getType() == RequestType.PICKUP) 
							? prevEjected
							: prevEjected.getSibling());
				
					insertedSuccessfully = insertion.insertRequestForVehicle(
							pickupToInsert, curVehicle, objective);
					
					/* if insertion is not possible 
					 * insert the request pair back into 
					 * the current vehicle's route */
					
					if (!insertedSuccessfully) {
						curVehicle.setRoute(routeCopy);
					} else {
						prevVehicle = curVehicle;
						prevEjected = curEjected;
					}
				}
			}
			
			/* we need to insert the last ejected
			 * request back into the last drawn vehicle
			 */
			
			pickupToInsert = (PickupRequest) 
					((prevEjected.getType() == RequestType.PICKUP) 
					? prevEjected
					: prevEjected.getSibling());
			
			Iterator<Vehicle> it = vehicles.iterator();
			insertedSuccessfully = false;
			
			while (it.hasNext() && !insertedSuccessfully) {
				insertedSuccessfully = insertion.insertRequestForVehicle(
						pickupToInsert, prevVehicle, objective);
				prevVehicle = it.next();
			}
			
			if (insertedSuccessfully) {
				it = vehicles.iterator();
				
				while (it.hasNext()) {
					Vehicle v = it.next();
					if (v.getRoute().getRequests().size() == 0) {
						it.remove();
					}
				}
				
				neighbor.setObjectiveValue(objective.calculate(neighbor));
				neighbors.add(neighbor);
			}
			
		}
		return neighbors;
	}

}
		
		
