package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.removal.RemovalAlgorithm;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class TabuOptimization implements OptimizationAlgorithm {
	private Solution solution;
	private AdaptiveMemory adaptiveMemory;
	private Configuration configuration;
	private AtomicBoolean shouldStop = new AtomicBoolean(false);
	
	@Override
	public Solution optimize() {
		LoggingUtils.info("Tabu optimization started (" + solution.getRequests().size() + " requests)");
		
		this.shouldStop.set(false);
		
		final int MAXI_TERATIONS = configuration.getIterations();
		solution.updateOjectiveValue( configuration.getAlgorithms().getObjective());
		Solution curSolution = solution;
		Solution bestSolution = solution;
		adaptiveMemory.addSolution(bestSolution);
		TabuList tabu = new TabuList(1000, configuration);
		int randomCreationRate = MAXI_TERATIONS / 10;
		
		for (int i = 0; i < MAXI_TERATIONS && !shouldStop.get() ; i++) {
			if (i % randomCreationRate == 0 && i != 0) {
				
				/* 0.65 is the treshold value
				 * it means that during the creation
				 * of the random solution the solutions
				 * from the top half of the list will
				 * be slightly prefered 
				 * 
				 * 3 is the number of iterations that
				 * should be executed during the selection
				 * of the sector of the solutions list
				 * that the random solution will be picked from 
				 * 
				 * more details can be found in the 
				 * comments in the AdaptiveMemory class */
				
				curSolution = adaptiveMemory.createRandomSolution(0.65, 3);
			}
			
			/* generate 15 nieighbors created using 
			 * ejection chains of maximum length 20 */
			
			final int iterationNo = i;
			Optional<Solution> bestNeighbor = generateNeighbors(curSolution, 15, 20, configuration)
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
//		LoggingUtils.info("Initial number of requests: " + solution.getRequests().size());
//		LoggingUtils.info("Number of requests: " + bestSolution.getRequests().size());
		LoggingUtils.info("Number of used vehicles: " + bestSolution.getVehicles().size());
		this.solution = bestSolution;
		
		return bestSolution;
	}
	
	/* the neighborhood is generated using
	 * so called ejection chains - 
	 * we remove a request from one route and insert it to 
	 * another one in the most feasible place;
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
			
			/* create a shallow copy of each vehicle;
			 * note that the route's requests list
			 * has to be set to a new list instation 
			 * so that we can safely modify it 
			 * without damaging the original route's list */
			
			List<Vehicle> vehicles = solution.getVehicles()
					.stream()
					.map(v -> v.copy())
					.collect(Collectors.toList());
			
			/* we need to replace the references to
			 * siblings with their copies */
			
			for (Vehicle v : vehicles) {
				Map<Integer, Request> requestsForIds = new HashMap<>();
				v.getRoute().getRequests()
						.stream()
						.filter(r -> r.getType() == RequestType.PICKUP)
						.collect(Collectors.toList())
						.forEach(p -> requestsForIds.put(p.getId(), p));
				
				v.getRoute().getRequests()
						.stream()
						.filter(r -> r.getType() == RequestType.DELIVERY)
						.collect(Collectors.toList())
						.forEach(d -> {
							Request p = requestsForIds.get(d.getSibling().getId());
							d.setSibling(p);
							p.setSibling(d);
						});
			}
			
			Solution neighbor = new Solution(vehicles);
			Vehicle prevVehicle = ListUtils.getRandomElement(vehicles);
			Request prevEjected = removal.removeRequestForVehicle(prevVehicle, configuration);
			Request curEjected;
			PickupRequest pickupToInsert;
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
							pickupToInsert, curVehicle, configuration);
					
					/* if insertion is not possible 
					 * restore the provious state 
					 * of the route */
					
					if (!insertedSuccessfully) {
						
						/* it is crucial to update the realization times
						 * because during the removal of a request its
						 * realization time is set to the beginning of
						 * its time window*/
						
						curVehicle.setRoute(routeCopy);
						curVehicle.updateRealizationTimes();
						
					} else {
						prevVehicle = curVehicle;
						prevEjected = curEjected;
					}
				}
			}
			
			/* we need to insert the last ejected
			 * request back into the last drawn vehicle
			 * (or any other vehicle in case it is
			 * not possible)
			 */
			
			pickupToInsert = (PickupRequest) 
					((prevEjected.getType() == RequestType.PICKUP) 
					? prevEjected
					: prevEjected.getSibling());
			
			Iterator<Vehicle> it = vehicles.iterator();
			insertedSuccessfully = false;
			
			while (it.hasNext() && !insertedSuccessfully) {
				insertedSuccessfully = insertion.insertRequestForVehicle(
						pickupToInsert, prevVehicle, configuration);
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

	@Override
	public synchronized Solution getSolution() {
		return this.solution;
	}

	@Override
	public synchronized AdaptiveMemory getAdaptiveMemory() {
		return this.adaptiveMemory;
	}

	@Override
	public OptimizationAlgorithm setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		return this;
	}

	@Override
	public OptimizationAlgorithm setSolution(Solution solution) {
		this.solution = solution;
		return this;
	}

	@Override
	public OptimizationAlgorithm setAdaptiveMemory(AdaptiveMemory adaptiveMemory) {
		this.adaptiveMemory = adaptiveMemory;
		return this;
	}

	@Override
	public void stopOptimization() {
		shouldStop.set(true);
	}

	@Override
	public OptimizationAlgorithm createShallowCopy() {
		return new TabuOptimization()
						.setConfiguration(configuration)
						.setSolution(solution)
						.setAdaptiveMemory(adaptiveMemory);
	}



}
		