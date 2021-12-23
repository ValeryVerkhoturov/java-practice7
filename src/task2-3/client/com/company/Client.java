package com.company;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client implements Runnable {

    static final int MAX_CATS_PER_SESSION = 10;
    static final int MIN_CATS_PER_SESSION = 5;

    Socket client;

    String name;

    List<Cat> cats;

    @Getter(onMethod_ = {@Synchronized}, value = AccessLevel.PRIVATE)
    ObjectInputStream objectInputStream;

    @Getter(onMethod_ = {@Synchronized}, value = AccessLevel.PRIVATE)
    ObjectOutputStream objectOutputStream;

    @SneakyThrows
    public static void main(String[] args) {
        @Cleanup FileReader fileReader = new FileReader("src/task2-3/resources/task2-3.properties");
        Properties properties = new Properties();
        properties.load(fileReader);

        List<Cat> randomCats = Stream
                .generate(Cat::newRandomInstance)
                .limit(ThreadLocalRandom.current()
                        .nextInt(MIN_CATS_PER_SESSION, MAX_CATS_PER_SESSION))
                .toList();
        new Client(properties.getProperty("hostname"),
                Integer.parseInt(properties.getProperty("port")),
                new ArrayList<>(randomCats))
                .run();
    }

    public Client(String host, int port, ArrayList<Cat> cats) {
        try {
            client = new Socket(host, port);
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectInputStream = new ObjectInputStream(client.getInputStream());

            this.cats = cats;
        } catch (IOException e) {
            System.out.println("Не запущен сервер");
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("hello");
        registration();

        new Thread(this::updateChat).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            final String message = scanner.nextLine();


        }
    }

    @SneakyThrows
    private void registration() {
        System.out.println(objectInputStream.readObject());

        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine();
        objectOutputStream.writeObject(MessageDTO.builder()
                .sender(name)
                .recipient("@server")
                .command(Command.SET_NAME)
                .message(name)
                .build());
    }

    @SneakyThrows
    private void updateChat() {
        while (true) {
            objectOutputStream.writeObject(MessageDTO.builder()
                    .sender(name)
                    .recipient("@server")
                    .command(Command.GET_LAST_MESSAGES)
                    .build());

            MessageDTO response = (MessageDTO) objectInputStream.readObject();
            System.out.println(response);
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }
}
