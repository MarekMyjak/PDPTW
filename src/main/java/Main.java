import model.Location;
import model.Request;

public class Main {
    public static void main(String[] args) {
        Location location = new Location(1, 2);
        Request request = Request.of(location, 3, 4, 5, 6, 7, 1);
    }
}
