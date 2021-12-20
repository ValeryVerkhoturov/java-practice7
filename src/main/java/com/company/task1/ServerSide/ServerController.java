package com.company.task1.ServerSide;

import lombok.SneakyThrows;
import lombok.Value;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Value
public class ServerController implements Runnable {

    int port;

    @SneakyThrows
    @Override
    public void run() {
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true)
            executorService.execute(new Session(serverSocket.accept()));
    }
}
