package org.hpham.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.OffsetDateTime;

public class Server {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }


    public static void createNewServer(int port) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api", new ApiHandler());

        server.setExecutor(null);

        server.start();
        System.out.printf("----Server is running on port %s---", port);
    }

    static class ApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                Thread.sleep(generateRandomLatency() * 1000L);
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

    private static int generateRandomLatency() {
        return (int) (Math.random() * 10);
    }

    record ApiResponse(String requestId, OffsetDateTime time) {}
}
