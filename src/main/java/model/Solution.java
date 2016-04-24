package model;

import lombok.Data;

import java.util.List;

@Data
public class Solution {
    private List<Route> routes;
    private List<Request> requests;

    public void remove(List<Request> requestToRemove) {
        this.requests.removeAll(requestToRemove);
    }
}
