package pl.edu.agh.io.pdptw.reader;

import pl.edu.agh.io.pdptw.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RequestReader {

    public LLBenchmark readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        Integer vehicleAmount = scanner.nextInt();
        Integer capacity = scanner.nextInt();
        Integer speed = scanner.nextInt();
        List<RequestLLB> requests = new LinkedList<>();

        while (scanner.hasNextInt()) {

            requests.add(RequestLLB.of(scanner.nextInt(), new Location(scanner.nextInt(), scanner.nextInt()),
                    scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
        }
        return LLBenchmark.of(vehicleAmount, capacity, speed, requests);
    }


}
