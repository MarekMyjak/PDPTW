package pl.edu.agh.io.pdptw.algorithm.dynamic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.Getter;
import pl.edu.agh.io.pdptw.algorithm.insertion.InsertionAlgorithm;
import pl.edu.agh.io.pdptw.algorithm.objective.Objective;
import pl.edu.agh.io.pdptw.algorithm.optimization.AdaptiveMemory;
import pl.edu.agh.io.pdptw.algorithm.optimization.DecompositionOptimizer;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;

public class RequestDispatcher {
	private static final int INSERTION_CHECK_RATE = 10;
	private static final int TIME_DELTA = 100;
	
	@Getter private List<Request> requests;
	@Getter private List<Vehicle> vehicles;
	@Getter private int vehiclesUsed;
	@Getter private Solution solution;
	@Getter private AtomicInteger time;
	@Getter private Configuration configuration;
	private InsertionWorker insertionWorker;
	private DecompositionOptimizer optimizer;
	private Thread optimizerThread;
	private ScheduledExecutorService executionService;
	
	private class InsertionWorker implements Runnable {
		private InsertionAlgorithm insertion;
		private Objective objective;
		
		public InsertionWorker() {
			this.insertion = configuration.getAlgorithms().getInsertionAlgorithm();
			this.objective = configuration.getAlgorithms().getObjective();
		}
		
		@Override
		public void run() {
			int curTime = time.addAndGet(TIME_DELTA);
			LoggingUtils.info("Current time: " + curTime);
			
			List<PickupRequest> pickups = requests.stream()
					.filter(r -> (r.getType() == RequestType.PICKUP)
							&& (r.getArrivalTime() <= curTime))
					.map(r -> (PickupRequest) r)
					.collect(Collectors.toList());
			
			/* remove all the requests being inserted in 
			 * this turn */
			
			requests = requests.stream()
					.filter(r -> r.getArrivalTime() > curTime)
					.collect(Collectors.toList());
					
			try {
				LoggingUtils.saveResult(solution, curTime, configuration);
			} catch (IOException e) {
				LoggingUtils.logStackTrace(e);
			}
			
			try {
				LoggingUtils.info("Attempting to stop the optimization thread");
				optimizer.stopOptimization();
				optimizerThread.join();
			} catch (InterruptedException e) {
				LoggingUtils.logStackTrace(e);
			}
				
			solution = optimizer.getSolution();
				
			/* remove the finished requests from the current
			 * solution */
			
			List<Integer> removedRequestsIds = solution.getVehicles()
					.stream()
					.flatMap(v -> v.removeFinishedRequests(curTime, true).stream())
					.map(r -> r.getId())
					.collect(Collectors.toList());
			
			/* remove the same requests from solutions 
			 * stored in the adaptive memory and
			 * reschedule the realization times */
					
			AdaptiveMemory adaptiveMemory = optimizer.getAdaptiveMemory();
			adaptiveMemory.getSolutions()
				.forEach(s -> s.getVehicles()
							.forEach(v -> {
								v.removeRequestsByIds(removedRequestsIds, curTime);
							}));
			
			if (pickups.size() > 0) {
				LoggingUtils.info("Inserting new requests");
				List<Vehicle> spareCopies = new LinkedList<>();
				boolean insertedSuccessfully;
				
				
				for (PickupRequest pickup : pickups) {
					LoggingUtils.info("Inserting: " + pickup.getId() 
							+ " (arrival time: " + pickup.getArrivalTime() + ")");
					
					/* note that we must copy the pickup request
					 * before inserting it to each of the
					 * solutions (in this way we'll avoid damaging
					 * the request data shared by multiple vehicles) */
					
					PickupRequest pickupCopy = (PickupRequest) pickup.copy();
					insertedSuccessfully = insertion.insertRequestToSolution(
							pickupCopy, solution, configuration);
					
					if (!insertedSuccessfully) {
						Vehicle spareVehicle = vehicles.get(vehiclesUsed);
						spareCopies.add(spareVehicle);	
						
						LoggingUtils.info("Vehicle [" + spareVehicle.getId() + "] has been used");
						insertion.insertRequestForVehicle(pickupCopy, spareVehicle, configuration);
						solution.getVehicles().add(spareVehicle);
						vehiclesUsed++;
					}
					
					solution.updateOjectiveValue(objective);
				}

				if (adaptiveMemory.getSolutions().size() > 0) {
					LoggingUtils.info("Updating adaptive memory (" 
							+ adaptiveMemory.getSolutions().size() +" solutions)");
					
					for (Solution s : adaptiveMemory.getSolutions()) {
						int spareCopiesUsed = 0;
						for (PickupRequest pickup : pickups) {
							
							/* note that we must copy the pickup request
							 * before inserting it to each of the
							 * solutions (in this way we'll avoid damaging
							 * the request data shared by multiple vehicles) */
							
							PickupRequest pickupCopy = (PickupRequest) pickup.copy();
							insertedSuccessfully = insertion.insertRequestToSolution(pickupCopy, s, configuration);
							
							if (!insertedSuccessfully) {
								Vehicle spareCopy;
								if (spareCopiesUsed >= spareCopies.size()) {
									spareCopy = vehicles.get(vehiclesUsed);
									spareCopies.add(spareCopy);
									vehiclesUsed++;
								} else {
									Vehicle spareVehicle = spareCopies.get(spareCopiesUsed);
									spareCopy = spareVehicle.copy();
									spareCopy.setRoute(new Route(new LinkedList<>()));
								}
								
								insertion.insertRequestForVehicle(pickupCopy, spareCopy, configuration);
								s.getVehicles().add(spareCopy);
								spareCopiesUsed++;
							}
						}
						
						s.updateOjectiveValue(objective);
					}
				}
			}
			
			/* remove all vehicles with no requests
			 * assigned to them */
			
			solution.setVehicles(solution.getVehicles()
					.stream()
					.filter(v -> v.getRoute().getRequests().size() > 0)
					.collect(Collectors.toList()));
			
			adaptiveMemory.getSolutions().forEach(
					s -> s.setVehicles(
							s.getVehicles().stream()
								.filter(v -> v.getRoute().getRequests().size() > 0)
								.collect(Collectors.toList())
							));
			
			/* print current requests for each vehicle from
			 * the chosen solution */
			
			for (Vehicle v : solution.getVehicles()) {
				if (v.getRoute().getRequests().size() > 0) {
					LoggingUtils.info("Vehicle ["
							+ v.getId() + "] is currently serving request [" 
							+ v.getCurrentRequest(curTime).getId() + "]");
				}
			}

			/* start the optizmiatin algorithm and
			 * reschedule the insertion task if there
			 * are more requests to insert or any 
			 * requests that must be finished */
			
			if (solution.getVehicles().size() > 1) {
				optimizer.setSolution(solution);
				optimizerThread = optimizer.startThread();
			}
			
			if (requests.size() > 0
					|| solution.getRequests().size() > 0) {
				LoggingUtils.info("Starting optimizer thread");
				
				/* reschedule this task again in <INSERTION_CHECK_RATE> seconds */
				
				LoggingUtils.info("Rescheduling the insertion task");
				executionService.schedule(this, INSERTION_CHECK_RATE, TimeUnit.SECONDS);
			}
		}
	}
	
	public RequestDispatcher(List<Request> requests, List<Vehicle> vehicles, Configuration configuration) {
		this.time = new AtomicInteger(0);
		this.configuration = configuration;
		
		/* if at the beginnning there are requests known 
		 * create a new solution based on them */
		
		this.solution = configuration.getAlgorithms()
				.getGenerationAlgorithm()
				.generateSolution(
						requests.stream()
						.filter(r -> r.getArrivalTime() == 0)
						.collect(Collectors.toList()) , vehicles, configuration);
		this.requests = requests.stream()
				.filter(r -> r.getArrivalTime() != 0)
				.collect(Collectors.toList());
		this.vehiclesUsed = solution.getVehicles().size();
		this.vehicles = vehicles.stream()
				.filter(v -> v.getRoute().getRequests().size() == 0)
				.collect(Collectors.toList());
		
		this.insertionWorker = new InsertionWorker();
		this.optimizer = new DecompositionOptimizer(solution, configuration);
		this.optimizerThread = this.optimizer.startThread();
		this.executionService = Executors.newScheduledThreadPool(1);
		this.executionService.schedule(this.insertionWorker, INSERTION_CHECK_RATE, TimeUnit.SECONDS);
	}
	
	
}
