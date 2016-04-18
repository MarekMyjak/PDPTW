package algorithms;

import model.Request;
import model.Solution;

import java.util.List;

public class InsertionHeuristic {
    private final InsertionService insertionService;

    public InsertionHeuristic(InsertionService insertionService) {
        this.insertionService = insertionService;
    }

    public Solution insertionGen(List<Request> requests){
        Solution solution = new Solution();
        requests.forEach(it -> insertionService.greedyInsert(solution, requests));
        return solution;
    }

    public Solution sweepGen(/*TODO*/){
        //TODO
        return new Solution();
    }

}
