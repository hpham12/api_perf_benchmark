package org.hpham.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelSync implements Requester {
    @Override
    public List<Server.ApiResponse> makeRequest(int numberOfRequests) {
        var threadPool = createNewThreadPool(3);
        List<Server.ApiResponse> responses = new ArrayList<>();
        for (int i = 0; i < numberOfRequests; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                try {
                    SynchronousClient.makeApiCall(finalI);
                } catch (URISyntaxException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        threadPool.shutdown();
        try {
            boolean terminateSuccess = threadPool.awaitTermination(5, TimeUnit.SECONDS);
            if (!terminateSuccess) {
                System.out.println("Failed to terminate within timeout period");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred");
        }


        return responses;
    }

    private static ExecutorService createNewThreadPool(int nThreads) {
        return Executors.newFixedThreadPool(nThreads);
    }

}
