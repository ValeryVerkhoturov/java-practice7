package com.company;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client implements Runnable {

    static final int MAX_CATS_PER_SESSION = 10;
    static final int MIN_CATS_PER_SESSION = 5;

    Socket client;

    String name;

    List<Cat> cats;

    @Getter(onMethod_ = @Synchronized)
    @Setter(onMethod_ = @Synchronized)
    boolean closed = false;

    ChatHistory chatHistory = new ChatHistory();

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
        registration();

        Thread chatUpdater = new Thread(this::updateChat);
        chatUpdater.setDaemon(true);
        chatUpdater.start();

        Scanner scanner = new Scanner(System.in);
        final String commands = "/commands - список команд; /close - close session with server; /";
        while (!isClosed()) {
            final String[] message = scanner.nextLine().split(" ");
            if (message.length == 0)
                continue;
            switch (message[0]) {
                case "/commands" -> System.out.println(commands);
                case "/close" -> objectOutputStream.writeObject(MessageDTO.builder()
                        .sender(name)
                        .recipient("@server")
                        .command(Command.CLOSE_SESSION)
                        .build());
                case "/sendcat" -> {
                    if (cats.size() > 0 && Objects.nonNull(message[1])) {
                        Cat cat = cats.remove(0);
                        objectOutputStream.writeObject(MessageDTO.builder()
                                .sender(name)
                                .recipient(message[1])
                                .command(Command.SEND_CAT)
                                .catToSend(cats.remove(0))
                                .build());
                        System.out.println("Отправлена " + cats.remove(0) + ". Осталось " + cats.size());
                    }
                    else if (Objects.isNull(message[1]))
                        System.out.println("Не указан ник получателя");
                    else
                        System.out.println("Кошек нет, но вы держитесь");
                }
                default -> objectOutputStream.writeObject(MessageDTO.builder()
                        .sender(name)
                        .recipient("@all")
                        .command(Command.SEND_MESSAGE)
                        .message(String.join(" ", message))
                        .build());
            }
        }
    }

    @SneakyThrows
    private void registration() {
        System.out.println(objectInputStream.readObject());

        Scanner scanner = new Scanner(System.in);
        name = scanner.nextLine().replace(" ", "");
        objectOutputStream.writeObject(MessageDTO.builder()
                .sender(name)
                .recipient("@server")
                .command(Command.SET_NAME)
                .message(name)
                .build());
    }

    @SneakyThrows
    private void updateChat() {
        while (!isClosed()) {
            objectOutputStream.writeObject(MessageDTO.builder()
                    .sender(name)
                    .recipient("@server")
                    .command(Command.GET_LAST_MESSAGES)
                    .build());

            MessageDTO response = (MessageDTO) objectInputStream.readObject();

            if (response.getCommand() == Command.CLOSE_SESSION)
                setClosed(true);

            chatHistory.update(response.getMessages()).forEach(System.out::println);


            TimeUnit.MILLISECONDS.sleep(200);
        }
    }
}
