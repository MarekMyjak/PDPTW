package pl.edu.agh.io.pdptw;
import pl.edu.agh.io.pdptw.model.DeliveryRequest;
import pl.edu.agh.io.pdptw.model.Location;
import pl.edu.agh.io.pdptw.model.PickupRequest;
import pl.edu.agh.io.pdptw.model.Request;

public class Main {

    public static void main(String[] args) {
    	Location location = new Location(2, 1);
    	DeliveryRequest req = new DeliveryRequest(location, 1, 1, 10, 5);
    	PickupRequest req2 = new PickupRequest(location, 3, 0, 9, 2);

//        Request request = new Request(location, 3, 4, 5, 6, 7, 1);
    }
}
