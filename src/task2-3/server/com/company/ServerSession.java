package com.company;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServerSession implements Runnable {

    @NonFinal
    @Getter(onMethod_ = {@Synchronized}) @Setter(onMethod_ = {@Synchronized})
    String name = "Unnamed";

    ChatHistory chatHistory;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    @SneakyThrows
    public ServerSession(Socket socket, ChatHistory chatHistory) {
        this.chatHistory = chatHistory;

        // Если поменять вызов outputStream и inputStream местами,
        // программа застрянет в конструкторе ObjectInputStream.
        // Задокументировано здесь https://docs.oracle.com/javase/6/docs/api/java/io/ObjectInputStream.html#ObjectInputStream%28java.io.InputStream%29
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    @SneakyThrows
    @Override
    public void run() {
        TimeUnit.SECONDS.sleep(10);
        objectOutputStream.writeObject(MessageDTO.builder()
                .message("Добро пожаловать на сервер Шизофрения. Введите имя.")
                .command(Command.OK)
                .build());
        objectOutputStream.flush();

        boolean sessionLoop = true;
        while (sessionLoop) {
            MessageDTO message = null;
            try {
                message = (MessageDTO) objectInputStream.readObject();
            } catch (SocketException e) {
                break;
            }
            switch (message.getCommand()) {
                case SET_NAME -> {
                    setName(message.getMessage());
                    chatHistory.addMessage(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .message("Имя установлено.")
                            .command(Command.OK)
                            .build());
                }
                case SEND_CAT ->
                    chatHistory
                            .addMessage(message)
                            .addMessage(MessageDTO.builder()
                                    .sender("@server")
                                    .recipient(message.getSender())
                                    .message("Кошка брошена в " + message.getRecipient())
                                    .command(Command.OK)
                                    .build());
                case SEND_MESSAGE ->
                    chatHistory
                            .addMessage(message)
                            .addMessage(MessageDTO.builder()
                                    .sender("@server")
                                    .recipient(message.getSender())
                                    .message("Сообщение отправлено")
                                    .command(Command.OK)
                                    .build());
                case CLOSE_SESSION -> {
                    sessionLoop = false;
                    chatHistory.addMessage(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .message("Сессия закрыта")
                            .command(Command.CLOSE_SESSION)
                            .build());
                }
                case GET_LAST_MESSAGES ->
                    objectOutputStream.writeObject(MessageDTO.builder()
                            .sender("@server")
                            .recipient(message.getSender())
                            .messages(chatHistory.getLastMessages(message))
                            .command(Command.SEND_LAST_MESSAGES)
                            .build());
                default -> objectOutputStream.writeObject(MessageDTO.builder()
                        .command(Command.ERROR)
                        .build());
            }
        }
    }
}
