package pl.edu.agh.io.pdptw.algorithm.optimization;

import java.util.List;

public class ListUtils {
	public static int getRandomIndex(List<?> list) {
		return (int) (Math.random() * (list.size() - 1));
	}
	
	public static <T> T getRandomElement(List<T> list) {
		return list.get((int) (Math.random() * (list.size() - 1)));
	}
}
