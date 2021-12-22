package com.company;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;

import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Value
@AllArgsConstructor(access = AccessLevel.NONE)
public class ServerController implements Runnable {

    ServerSocket serverSocket;

    ExecutorService executorService = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

    @SneakyThrows
    public ServerController(int port) {
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!serverSocket.isClosed())
            try {
                executorService.execute(new Session(serverSocket.accept()));
            } catch (SocketException ignore) {}
    }

    @SneakyThrows
    public void stop() {
        executorService.shutdown();
        serverSocket.close();
        Thread.currentThread().interrupt();
    }
}
