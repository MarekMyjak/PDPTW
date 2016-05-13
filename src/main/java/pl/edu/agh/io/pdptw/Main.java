package pl.edu.agh.io.pdptw;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import pl.edu.agh.io.pdptw.configuration.Configuration;
import pl.edu.agh.io.pdptw.configuration.DefaultConfigReader;
import pl.edu.agh.io.pdptw.logging.LoggingUtils;
import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.reader.exception.InvalidFileFormatException;


public class Main {

    public static void main(String[] args) throws IOException {

    	DefaultConfigReader configReader = new DefaultConfigReader();
    	List<Request> requests;
    	List<Configuration> testConfigurations;
    	
    	try {
    		testConfigurations = configReader.loadConfiguration("resources/test/li_lim_benchmark/config.json");
    		Configuration configuration = testConfigurations.get(0);
    		Vehicle.setScheduler(configuration.getAlgorithms().getScheduler());
			requests = configReader.loadRequests(configuration);
			List<Vehicle> vehicles = configReader.loadVehicles(configuration);
			
			for (Request r : requests) {
				System.out.println(r);
			}
			for (Vehicle v : vehicles) {
				System.out.println(v);
			}
			
		} catch (InvalidFileFormatException | ParseException | IllegalArgumentException e) {
			LoggingUtils.logStackTrace(e);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
			LoggingUtils.error("An error occurred while reading input file");
		}

	}
}
