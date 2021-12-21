package com.company.task1;

import com.company.task1.ClientSide.ClientController;
import com.company.task1.ServerSide.ServerController;
import lombok.Cleanup;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

final class Task1 {

    private static final Properties properties = new Properties();

    public static void main(String[] args) throws IOException, InterruptedException {
        @Cleanup FileReader fileReader = new FileReader("src/main/resources/task1.properties");
        properties.load(fileReader);

        ServerController serverController = new ServerController(Integer.parseInt(properties.getProperty("port")));
        new Thread(serverController).start();

        TimeUnit.SECONDS.sleep(1);
        Thread client = new Thread(new ClientController(properties.getProperty("hostname"), Integer.parseInt(properties.getProperty("port"))));
        client.start();
        client.join();

        interruptServer(serverController);
    }

    private static void interruptServer(ServerController serverController) {
        @Cleanup Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Остановить сервер? y/n");
        } while (!scanner.nextLine().equals("y"));

        serverController.stop();
    }
}
