package com.company;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ServerSession implements Runnable {

    @NonFinal
    @Getter(onMethod_ = {@Synchronized}) @Setter(onMethod_ = {@Synchronized})
    String name;

    Socket socket;

    ChatHistory chatHistory;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    static int MAX_CATS_PER_SESSION = 10;
    static int MIN_CATS_PER_SESSION = 5;

    @Getter(onMethod_ = {@Synchronized})
    List<Cat> cats;

    @SneakyThrows
    public ServerSession(Socket socket, ChatHistory chatHistory) {
        this.socket = socket;
        this.chatHistory = chatHistory;

        objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());

        cats = new ArrayList<>(
                Stream.generate(Cat::newRandomInstance)
                        .limit(ThreadLocalRandom.current().nextInt(MIN_CATS_PER_SESSION, MAX_CATS_PER_SESSION))
                        .toList());
    }

    @SneakyThrows
    @Override
    public void run() {
        registration();

        boolean sessionLoop = true;
        while (sessionLoop) {
            final MessageDTO message = (MessageDTO) objectInputStream.readObject();
            switch (message.getCommand()) {
                case SET_NAME -> {
                    setName(message.getMessage());
                    objectOutputStream.writeObject(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .message("Имя установлено.")
                            .command(Command.SET_NAME)
                            .build());
                }
                case SEND_CAT -> {
                    chatHistory.addMessage(message);
                    objectOutputStream.writeObject(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .message("Кошка брошена в " + message.getRecipient())
                            .command(Command.SEND_CAT)
                            .build());
                }
                case SEND_MESSAGE -> {
                    chatHistory.addMessage(message);
                    objectOutputStream.writeObject(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .message("Сообщение отправлено")
                            .command(Command.SEND_MESSAGE)
                            .build());
                }
                case GET_LAST_MESSAGES ->
                    objectOutputStream.writeObject(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .messages(chatHistory.getLastMessages(message))
                            .command(Command.GET_LAST_MESSAGES)
                            .build());
                case CLOSE_SESSION -> {
                    sessionLoop = false;
                    objectOutputStream.writeObject(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .message("Сессия закрыта")
                            .command(Command.CLOSE_SESSION)
                            .build());
                }
                default -> objectOutputStream.writeObject(MessageDTO.builder()
                        .command(Command.ERROR)
                        .build());
            }


        }
    }

    @SneakyThrows
    private void registration() {
        objectOutputStream.writeObject(MessageDTO.builder()
                .message("Добро пожаловать на сервер Шизофрения. Введите имя.")
                .command(Command.SET_NAME)
                .build());
        MessageDTO message = (MessageDTO) objectInputStream.readObject();
        if (message.getCommand() == Command.SEND_CAT)
            setName(message.getMessage());
    }
}
