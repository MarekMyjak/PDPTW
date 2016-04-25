package pl.edu.agh.io.pdptw.algorithm.removal;

import pl.edu.agh.io.pdptw.model.Request;
import pl.edu.agh.io.pdptw.model.Route;
import pl.edu.agh.io.pdptw.model.Solution;

import java.util.List;
import java.util.Set;

public class RandomRemoval implements RemovalAlgorithm {

	@Override
	public Request removeRequest(Solution solution) {
		Set<Route> routeList = solution.getRoutes();

		return null;
	}

}
