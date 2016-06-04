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
import pl.edu.agh.io.pdptw.algorithm.optimization.OptimizationWorker;
import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.RequestType;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.visualization.VisualizationService;

public class RequestDispatcher {
	private static final int INSERTION_CHECK_RATE = 5;
	
	@Getter private List<Request> requests;
	@Getter private List<Vehicle> vehicles;
	@Getter private int vehiclesUsed;
	@Getter private Solution solution;
	@Getter private AtomicInteger time;
	@Getter private Configuration configuration;
	private AdaptiveMemory adaptiveMemory;
	private InsertionWorker insertionWorker;
	private OptimizationWorker optimizationWorker;
	private Thread optimizationThread;
	private VisualizationService visualizationService;
	
	private class InsertionWorker implements Runnable {
		private InsertionAlgorithm insertion;
		private Objective objective;
		
		public InsertionWorker() {
			this.insertion = configuration.getAlgorithms().getInsertionAlgorithm();
			this.objective = configuration.getAlgorithms().getObjective();
		}
		
		@Override
		public void run() {
			int curTime = time.addAndGet(INSERTION_CHECK_RATE);
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
			
			if (pickups.size() > 0) {
				LoggingUtils.info("New requests have arrived");
				optimizationWorker.stopOptimization();
				LoggingUtils.info("Attempting to stop the optimization thread");
				
				try {
					optimizationThread.join();
				} catch (InterruptedException e) {
					LoggingUtils.logStackTrace(e);
				}
				
				LoggingUtils.info("Inserting new requests");
				List<Vehicle> spareCopies = new LinkedList<>();
				boolean insertedSuccessfully;
				
				for (PickupRequest pickup : pickups) {
					LoggingUtils.info("Inserting: " + pickup.getId());
					insertedSuccessfully = insertion.insertRequestToSolution(
							pickup, solution, objective);
					
					if (!insertedSuccessfully) {
						Vehicle spareVehicle = vehicles.get(vehiclesUsed);
						spareCopies.add(spareVehicle);	
						
						LoggingUtils.info("Vehicle [" + spareVehicle.getId() + "] has been used");
						
						spareVehicle.insertRequest(pickup, 0, 1);
						solution.getVehicles().add(spareVehicle);
						vehiclesUsed++;
					}
					
					solution.updateOjectiveValue(objective);
				}
				
//				if (adaptiveMemory.getSolutions().size() > 0) {
//					LoggingUtils.info("Updating adaptive memory (" 
//							+ adaptiveMemory.getSolutions().size() +" solutions)");
//					int i = 0;
//					for (Solution s : adaptiveMemory.getSolutions()) {
//						LoggingUtils.info("Solution: " + i);
//						i++;
//						int spareCopiesUsed = 0;
//						for (PickupRequest pickup : pickups) {
//							insertedSuccessfully = insertion.insertRequestToSolution(pickup, s, objective);
//							
//							System.out.println("inserted");
//							
//							if (!insertedSuccessfully) {
//								Vehicle spareCopy;
//								System.out.println("Solution: " + i);
//								if (spareCopiesUsed > spareCopies.size()) {
//									spareCopy = vehicles.get(vehiclesUsed);
//									spareCopies.add(spareCopy);
//									vehiclesUsed++;
//									System.out.println("new spare: " + i);
//								} else {
//									Vehicle spareVehicle = spareCopies.get(spareCopiesUsed - 1);
//									spareCopy = spareVehicle.createShallowCopy();
//									spareCopy.setLocation(spareCopy.getStartLocation());
//									spareCopy.setCurrentlyLoaded(0);
//									spareCopy.setRoute(new Route(new LinkedList<>()));
//									System.out.println("old spare");
//								}
//								
//								System.out.println("insering");
//								spareCopy.insertRequest(pickup, 0, 1);
//								s.getVehicles().add(spareCopy);
//								spareCopiesUsed++;
//								System.out.println("loop done");
//							}
//							
//						}
//						
//						s.updateOjectiveValue(objective);
//					}
//				}
				
				optimizationWorker.getOptimization().setAdaptiveMemory(adaptiveMemory);
				optimizationWorker.getOptimization().setSolution(solution);
				
				try {
					visualizationService.makeVisualizationData(solution, configuration);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				LoggingUtils.info("Starting optimization thread");
				optimizationThread = new Thread(optimizationWorker);
				optimizationThread.start();
			}
		}
	}
	
	public RequestDispatcher(List<Request> requests, List<Vehicle> vehicles, Configuration configuration) {
		this.requests = requests;
		this.time = new AtomicInteger(0);
		this.configuration = configuration;
		this.visualizationService = new VisualizationService();
		
		/* if at the beginnning there are requests known 
		 * create a new solution based on them */
		
		this.solution = configuration.getAlgorithms()
				.getGenerationAlgorithm()
				.generateSolution(
						requests.stream()
						.filter(r -> r.getArrivalTime() == 0)
						.collect(Collectors.toList()) , vehicles, configuration);
		this.vehiclesUsed = solution.getVehicles().size();
		this.vehicles = vehicles.stream()
				.filter(v -> v.getRoute().getRequests().size() == 0)
				.collect(Collectors.toList());
		
		this.insertionWorker = new InsertionWorker();
		this.optimizationWorker = new OptimizationWorker(solution, configuration);
		this.optimizationThread = new Thread(this.optimizationWorker);
		this.optimizationThread.start();
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(
				this.insertionWorker, 10, INSERTION_CHECK_RATE, TimeUnit.SECONDS);
	}
	
	
}
