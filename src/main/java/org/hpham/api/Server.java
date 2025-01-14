package org.hpham.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.OffsetDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }


    public static void createNewServer(int port) throws IOException{
        Executor threadPool = Executors.newFixedThreadPool(5);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(threadPool);

        server.createContext("/api", new ApiHandler());

        server.start();
        System.out.printf("----------Server is running on port %s---------\n", port);
    }

    static class ApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error occurred");
                return;
            }

            // extract path variable
            String requestId = httpExchange.getRequestURI().getPath().split("/")[2];
            ApiResponse response = new ApiResponse(requestId, OffsetDateTime.now());
            httpExchange.sendResponseHeaders(200, 0);
            OutputStream os = httpExchange.getResponseBody();
            os.write(objectMapper.writeValueAsBytes(response));
            os.close();
        }
    }

    public record ApiResponse(String requestId, OffsetDateTime time) {}
}
