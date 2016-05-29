package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestPositions;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class TabuOptimization implements OptimizationAlgorithm {
	private static final int MAX_ITERATIONS = 100;
	private Configuration configuration;
	
	@Override
	public void optimize(Solution solution, Configuration configuration) {
		for (int i = 0; i < MAX_ITERATIONS; i++) {
			
		}
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
	
	public List<Solution> generateNeighbors(Solution solution, int n, Configuration configuration) {
		List<Solution> neighbors = new LinkedList<>();
				
		InsertionAlgorithm insertion = configuration.getAlgorithms().getInsertionAlgorithm();
		Objective objective = configuration.getAlgorithms().getObjective();
		int maxChainLength = 10;
		
		/* generate n new solutions based on the passed one */
		for (int i = 0; i < n; i++) {
			
			/* create shallow copy of each vehicle
			 * note that the route's requests list
			 * has to be set to a new list instation 
			 * so that we can safely modify it 
			 * without damaging the original route's list */
			
			List<Vehicle> vehicles = solution.getVehicles()
					.stream()
					.map(v -> {
						Vehicle copy = v.createShallowCopy();
						copy.setRoute(new Route(
								new LinkedList<>(
										copy.getRoute().getRequests())));
						return copy;
					})
					.collect(Collectors.toList());
			
			/* for conviniece purposes create
			 * a list of the newly created routes 
			 * instantions bound to the vehicles copies*/
			
			Vehicle prevVehicle = ListUtils.getRandomElement(vehicles);
			
			for (int j = 0; j < maxChainLength; j++) {
				Vehicle nextVehicle = ListUtils.getRandomElement(vehicles);
				Request ejected =  ListUtils.getRandomElement(prevVehicle.getRoute().getRequests());
				PickupRequest pickup = (PickupRequest) ((ejected.getType() == RequestType.PICKUP) 
						? ejected
						: ejected.getSibling());
				
				RequestPositions position = 
						prevVehicle.removeRequest(pickup);
				System.out.println("Removing from : " + prevVehicle.getId() + " id: " + pickup.getId());
				System.out.println("Inserting to: " + nextVehicle.getId());
				insertion.insertRequestForVehicle(pickup, nextVehicle, objective);
			}
			
			neighbors.add(new Solution(vehicles));
			System.out.println("--------------------");
		}
		
		return neighbors;
	}

}
		
		
