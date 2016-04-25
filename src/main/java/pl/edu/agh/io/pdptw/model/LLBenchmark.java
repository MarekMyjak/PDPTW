package pl.edu.agh.io.pdptw.model;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class LLBenchmark {
    Integer vehicleAmount;
    Integer capacity;
    Integer speed;
    List<RequestLLB> requests;
}
