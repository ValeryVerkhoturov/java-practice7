package com.company;

import lombok.Cleanup;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

class ServerLauncher {

    private static final Properties properties = new Properties();

    public static void main(String[] args) throws IOException {
        @Cleanup FileReader fileReader = new FileReader("src/task1/resources/task1.properties");
        properties.load(fileReader);

        ServerController serverController = new ServerController(Integer.parseInt(properties.getProperty("port")));
        new Thread(serverController).start();

        askAndStopServer(serverController);
    }

    private static void askAndStopServer(ServerController serverController) {
        @Cleanup Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Остановить сервер? y/n");
        } while (!scanner.nextLine().equals("y"));

        serverController.stop();
    }
}
