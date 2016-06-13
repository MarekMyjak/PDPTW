package pl.edu.agh.io.pdptw.logging;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.visualization.VisualizationService;

public class LoggingUtils {
	private static Logger logger = LogManager.getRootLogger();
	private static StringWriter sw = new StringWriter();
	private static PrintWriter pw = new PrintWriter(sw);
	private static VisualizationService visualizationService = new VisualizationService();
	
	static {
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(new PatternLayout("[%p] %d - %m%n"));
		consoleAppender.setThreshold(Level.INFO);
		consoleAppender.activateOptions();
		logger.addAppender(consoleAppender);
	}
	
	public static void configure(Configuration configuration) {
		FileAppender fileAppender = new FileAppender();
		fileAppender.setName("FileLogger");
		fileAppender.setFile("optimization.log");
		fileAppender.setLayout(new PatternLayout("[%p] %d %c - %m%n"));
		fileAppender.setThreshold(Level.INFO);
		fileAppender.setAppend(true);
		fileAppender.activateOptions();
		logger.addAppender(fileAppender);
		
		String[] requestsPathElements = configuration.getRequestsPath().split("/");
        String requestsFileName = requestsPathElements[requestsPathElements.length - 1];  
		String optimizationLogFileName = configuration.getOutputPath() 
				+ requestsFileName 
				+ "_" + configuration.getIterations()
				+ "_" + configuration.getAlgorithms().getGenerationAlgorithm()
					.getClass().getSimpleName() + "_" + "optimization.log";
		
		fileAppender.setFile(optimizationLogFileName);
		logger.addAppender(fileAppender);
	}
	
	public static void info(Object message) {
		logger.info(message.toString());
	}
	
	public static void debug(Object message) {
		logger.debug(message.toString());
	}
	
	public static void warn(Object message) {
		logger.warn(message.toString());
	}
	
	public static void error(Object message) {
		logger.error(message.toString());
	}
	
	public static void fatal(Object message) {
		logger.fatal(message.toString());
	}
	
	public static String getStackTraceAsString(Throwable throwable) {
		sw.flush();
		throwable.printStackTrace(pw);
		
		return sw.toString();
	}
	
	public static void logStackTrace(Throwable throwable) {
		logger.error(getStackTraceAsString(throwable));
	}
	
	public static void saveResult(Solution solution, Configuration configuration) throws IOException {
        String[] requestsPathElements = configuration.getRequestsPath().split("/");
        String requestsFileName = requestsPathElements[requestsPathElements.length - 1];  
		String pathPrefix = configuration.getOutputPath() 
				+ requestsFileName 
				+ "_" + configuration.getIterations()
				+ "_" + configuration.getAlgorithms().getGenerationAlgorithm()
					.getClass().getSimpleName() + "_";
		
		String routesFilePath =  pathPrefix + "routes.txt";
		String solutionDetailsFilePath =  pathPrefix + "solutionDetails.txt";
		
		StringBuilder builder = new StringBuilder();
//		builder.append("Instance name: " + requestsFileName + "\r\n");
//		builder.append("Date: " + LocalDate.now() + "\r\n");
//		builder.append("Solution: \r\n");
//		
//		for (Vehicle v : solution.getVehicles()) {
//			builder.append(v.getId() + ": " 
//					+ (String.join(", ", v.getRoute().getRequests()
//							.stream()
//							.map(r -> "" + r.getId())
//							.collect(Collectors.toList()))) + "\r\n");
//		}
//		
//		try (PrintWriter out = new PrintWriter(routesFilePath)) {
//			out.print(builder.toString());
//		} catch (FileNotFoundException e) {
//			logStackTrace(e);
//		}
		
		builder = new StringBuilder();
		builder.append("Date: " + LocalDate.now() + "\r\n");
		builder.append(configuration.toString() + "\r\n");
		builder.append("\r\nObjective value: " + solution.getObjectiveValue() + "\r\n");
		builder.append("Vehicles used: " + solution.getVehicles().size() + "\r\n");
		builder.append("\r\n" + solution.toString() + "\r\n");
		
		try (PrintWriter out = new PrintWriter(solutionDetailsFilePath)) {
			out.print(builder.toString());
		} catch (FileNotFoundException e) {
			logStackTrace(e);
		}
		
		visualizationService.makeVisualizationData(solution, configuration);
	}
	
	public static void saveResult(Solution solution, int time, Configuration configuration) throws IOException {
        String[] requestsPathElements = configuration.getRequestsPath().split("/");
        String requestsFileName = requestsPathElements[requestsPathElements.length - 1];  
		String pathPrefix = configuration.getOutputPath() 
				+ requestsFileName 
				+ "_" + configuration.getIterations()
				+ "_" + configuration.getAlgorithms().getGenerationAlgorithm()
					.getClass().getSimpleName() + "_";
		
		String routesFilePath =  pathPrefix + "routes.txt";
		String solutionDetailsFilePath =  pathPrefix + "solutionDetails_" + time + ".txt";
		
		StringBuilder builder = new StringBuilder();
//		builder.append("Instance name: " + requestsFileName + "\r\n");
//		builder.append("Date: " + LocalDate.now() + "\r\n");
//		builder.append("Solution: \r\n");
//		
//		for (Vehicle v : solution.getVehicles()) {
//			builder.append(v.getId() + ": " 
//					+ (String.join(", ", v.getRoute().getRequests()
//							.stream()
//							.map(r -> "" + r.getId())
//							.collect(Collectors.toList()))) + "\r\n");
//		}
//		
//		try (PrintWriter out = new PrintWriter(routesFilePath)) {
//			out.print(builder.toString());
//		} catch (FileNotFoundException e) {
//			logStackTrace(e);
//		}
		
		builder = new StringBuilder();
		builder.append("Date: " + LocalDate.now() + "\r\n");
		builder.append(configuration.toString() + "\r\n");
		builder.append("\r\nObjective value: " + solution.getObjectiveValue() + "\r\n");
		builder.append("Vehicles used: " + solution.getVehicles().size() + "\r\n");
		builder.append("Requests: " + solution.getRequests().size() + "\r\n");
		builder.append("\r\n" + solution.toString() + "\r\n");
		
		try (PrintWriter out = new PrintWriter(solutionDetailsFilePath)) {
			out.print(builder.toString());
		} catch (FileNotFoundException e) {
			logStackTrace(e);
		}
		
		visualizationService.makeVisualizationData(solution, time, configuration);
	}
}
