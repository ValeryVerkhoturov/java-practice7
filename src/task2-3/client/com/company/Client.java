package com.company;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Client implements Runnable{

    Socket client;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup FileReader fileReader = new FileReader("src/task2-3/resources/task2-3.properties");
        Properties properties = new Properties();
        properties.load(fileReader);

        new Client(properties.getProperty("hostname"),
                Integer.parseInt(properties.getProperty("port"))).run();
    }

    @SneakyThrows
    public Client(String host, int port) {
        try {
            client = new Socket(host, port);
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        } catch (ConnectException e) {
            System.out.println("Не запущен сервер");
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {

        }
    }
}
