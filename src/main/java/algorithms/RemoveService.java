package algorithms;

import model.Request;
import model.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RemoveService {
    private Random random = new Random();

    public void randomRemoval(Solution solution, Integer k){
        List<Request> requests = solution.getRequests();
        List<Request> requestToRemove = new LinkedList<>();
        while (k>0){
            Request request = requests.get((int) Math.floor(random.nextDouble() * requests.size()));
            requests.remove(request);
            requestToRemove.add(request);
            k--;
        }
        solution.remove(requestToRemove);
    }

    public void worstRemoval(Solution solution, Integer k, Integer p){
        List<Request> requests = solution.getRequests();
        List<Request> requestToRemove = new LinkedList<>();
        while (k>0){
            Request request = requests.get((int) Math.floor(Math.pow(random.nextDouble(),p) * requests.size()));
            requests.remove(request);
            requestToRemove.add(request);
            k--;
        }
        solution.remove(requestToRemove);
    }

    public void shawRemoval(Solution solution, Integer k, Integer p){
        //TODO
    }
}
