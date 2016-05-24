package pl.edu.agh.io.pdptw.configuration;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.reader.exception.InvalidFileFormatException;

public interface ConfigReader {
	List<Configuration> loadConfiguration(String configFilePath)
			throws IllegalArgumentException, IOException, ParseException;
	List<Request> loadRequests(Configuration configuration)
			throws IOException, InvalidFileFormatException;
	List<Vehicle> loadVehicles(Configuration configuration)
			throws IOException, ParseException;
}
