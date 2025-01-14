package org.hpham.api;

import java.util.ArrayList;
import java.util.List;

public class Sequential implements Requester {
    @Override
    public List<Server.ApiResponse> makeRequest(int numberOfRequests) {
        List<Server.ApiResponse> responses = new ArrayList<>();
        for (int i = 0; i < numberOfRequests; i++) {
            try {
                var res = SynchronousClient.makeApiCall(i);
                responses.add(res);
            } catch (Exception e) {
                System.out.printf("Error occurred when calling API with id %d\n", i);
            }
        }

        return responses;
    }
}
