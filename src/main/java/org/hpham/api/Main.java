package org.hpham.api;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server.createNewServer(8000);
        } catch (IOException e) {
            System.out.println("Server failed to start");
        }

        long startTime = System.currentTimeMillis();
        makeRequestsSequentially(5);
        System.out.printf("Time take to make requests sequentially: %s\n", System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        makeRequestsParallelSync(5);
        System.out.printf("Time take to make requests parallel sync: %s\n", System.currentTimeMillis() - startTime);
    }


    public static void makeRequestsSequentially(int requests) {
        Sequential sequentialRequester = new Sequential();
        var res = sequentialRequester.makeRequest(requests);
        if (res.size() != requests) {
            System.out.println("Error occurred while making requests sequentially");
        }
    }

    public static void makeRequestsParallelSync(int requests) {
        Requester parallelSyncRequester = new ParallelSync();
        var res = parallelSyncRequester.makeRequest(requests);
    }
}