package pl.edu.agh.io.pdptw.algorithm.optimization;

import org.junit.Test;
import pl.edu.agh.io.pdptw.model.Solution;
import pl.edu.agh.io.pdptw.test.util.DataGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AdaptiveMemoryRandomSolutionIndicesTest {

	@Test
	public void test() {
		Solution s1 = DataGenerator.generateSolution(12);
		Solution s2 = DataGenerator.generateSolution(6);
		Solution s3 = DataGenerator.generateSolution(10);
		
		List<Solution> solutions = Arrays.asList(s1, s2, s3);
		
		List<Integer> solutionIndices = IntStream.range(0, solutions.size())
				.boxed()
				.collect(Collectors.toList());
		
		Map<Integer, List<Integer>> routeIndices = new HashMap<>();
		
		for (Integer i : solutionIndices) {
			
			/* .boxed() transforms the IntStream to a Stream<Integer>
			 * which can be collected in the form 
			 * of a list */
			
			List<Integer> routeIds = IntStream
					.range(0, solutions
							.get(i)
							.getRoutes()
							.size())
					.boxed()
					.collect(Collectors.toList());
			routeIndices.put(i, routeIds);
		}
		
		System.out.println(solutionIndices);
		routeIndices.entrySet().forEach(e -> System.out.println("" + e.getKey() + ": " + e.getValue()));
	}

}
