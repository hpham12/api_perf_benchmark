package org.hpham.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Client {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static Server.ApiResponse makeApiCall(int requestId) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format("http://localhost:8000/api/%s", requestId)))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> res = client.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(res.body(), Server.ApiResponse.class);
    }

//    public static CompletableFuture<Server.ApiResponse> makeAsyncApiCall(int requestId) throws URISyntaxException, IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI(String.format("http://localhost:8000/api/%s", requestId)))
//                .GET()
//                .build();
//
//        HttpClient client = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .build();
//
//        CompletableFuture<HttpResponse<String>> res = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
//        CompletableFuture.suppl
//
//        return objectMapper.readValue(res.body(), Server.ApiResponse.class);
//    }
}
