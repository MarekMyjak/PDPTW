package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Vehicle;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

public class ListCopyTest {

	@Test
	public void test() {
		Vehicle v = DataGenerator.generateVehicle(5);
		List<Request> reqs = new ArrayList<>(v.getRoute().getRequests());
		
		System.out.println(reqs.remove(0));
		System.out.println(v);
		reqs.forEach(r -> System.out.println(r));
	}

}
