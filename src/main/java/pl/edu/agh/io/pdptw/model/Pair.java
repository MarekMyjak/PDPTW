package pl.edu.agh.io.pdptw.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode

public class Pair<T, U> {
	T first;
	U second;
}
