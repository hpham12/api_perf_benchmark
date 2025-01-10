package org.hpham.api;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server.createNewServer(8000);
        } catch (IOException e) {
            System.out.println("Server failed to start");
        }
    }
}