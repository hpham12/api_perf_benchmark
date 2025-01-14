package org.hpham.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParallelAsync implements Requester{
    @Override
    public List<Server.ApiResponse> makeRequest(int numberOfRequests) {
        List<CompletableFuture<Server.ApiResponse>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfRequests; i++) {
            int finalI = i;
            CompletableFuture<Server.ApiResponse> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return Client.makeApiCall(finalI);
                } catch (URISyntaxException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            futures.add(future);
        }

        CompletableFuture.allOf((futures.toArray(new CompletableFuture[0])));
        return futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
