package reader;

import model.Location;
import model.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RequestReader {
    public List<Request> readAll(File file) throws FileNotFoundException {
        List<Request> requests = new LinkedList<Request>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextInt()){
            requests.add(Request.of(new Location(scanner.nextInt(), scanner.nextInt()), scanner.nextInt(),
                    scanner.nextInt(), scanner.nextInt(),scanner.nextInt(), scanner.nextInt(),scanner.nextInt()));
        }
        return requests;
    }
}
