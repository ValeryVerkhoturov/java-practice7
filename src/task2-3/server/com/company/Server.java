package com.company;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.FileReader;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Server implements Runnable {

    ServerSocket serverSocket;

    ChatHistory chatHistory = new ChatHistory();

    ExecutorService executorService = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });

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
            try {
                executorService.execute(new ServerSession(serverSocket.accept(), chatHistory));
            } catch (SocketException ignore) {}
        }
    }
}
