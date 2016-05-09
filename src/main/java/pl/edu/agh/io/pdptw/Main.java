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

//        RequestReader requestReader = new RequestReader();
//        File file = new File("Li & Lim benchmark/readerTest/lc101.txt");
//        System.out.println(file.getCanonicalPath());
//        LLBenchmark benchmark = requestReader.readFile(file);
        
    	DefaultConfigReader configReader = new DefaultConfigReader();
    	List<Request> requests;
    	List<Configuration> testConfigurations;
    	
    	try {
    		testConfigurations = configReader.loadConfiguration("resources/test/li_lim_benchmark/config.json");
    		Configuration configuration = testConfigurations.get(0);
			requests = configReader.loadRequests(configuration.getRequestsPath());
			List<Vehicle> vehicles = configReader.loadVehicles(configuration.getVehiclesPath());
			
			for (Request r : requests) {
				System.out.println(r);
			}
			for (Vehicle v : vehicles) {
				System.out.println(v);
			}
			
		} catch (InvalidFileFormatException e) {
			LoggingUtils.logStackTrace(e);
		} catch (ParseException e) {
			LoggingUtils.logStackTrace(e);
		} catch (IOException e) {
			LoggingUtils.logStackTrace(e);
			LoggingUtils.error("An error occurred while reading input file");
		} catch (IllegalArgumentException e) {
			LoggingUtils.logStackTrace(e);
		}
    	
    }
}
