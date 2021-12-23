package com.company;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.FileReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Server implements Runnable {

    ServerSocket serverSocket;

    List<String> chatHistory = Collections.synchronizedList(new ArrayList<>());

    List<ServerSession> sessions = new ArrayList<>();

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup FileReader fileReader = new FileReader("src/task2-3/resources/task2-3.properties");
        Properties properties = new Properties();
        properties.load(fileReader);

        new Server(Integer.parseInt(properties.getProperty("port"))).run();
    }

    @SneakyThrows
    public Server(int port) {
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            sessions.add(new ServerSession(serverSocket.accept()));
        }
    }
}