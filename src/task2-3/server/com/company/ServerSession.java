package com.company;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServerSession implements Runnable, Closeable {

    Socket socket;

    ObjectInputStream objectInputStream;

    @Getter
    ObjectOutputStream objectOutputStream;

    @NonFinal
    @Setter @Getter
    boolean sessionLoop = true;

    @NonFinal
    @Getter(onMethod_ = @Synchronized) @Setter(onMethod_ = @Synchronized)
    String name = "Unnamed";

    @Getter
    ChatHistory chatHistory;

    @SneakyThrows
    public ServerSession(Socket socket, ChatHistory chatHistory) {
        this.socket = socket;
        this.chatHistory = chatHistory;

        // Если поменять вызов конструкторов outputStream и inputStream местами,
        // программа застрянет в конструкторе ObjectInputStream.
        // Задокументировано здесь https://docs.oracle.com/javase/6/docs/api/java/io/ObjectInputStream.html#ObjectInputStream%28java.io.InputStream%29
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    @SneakyThrows
    @Override
    public void run() {
        ResponseBehavior.greetings(this);

        while (sessionLoop) {
            try {
                MessageDTO message = (MessageDTO) objectInputStream.readObject();
                switch (message.getCommand()) {
                    case SET_NAME -> ResponseBehavior.setName(this, message);
                    case SEND_CAT -> ResponseBehavior.sendCat(this, message);
                    case SEND_MESSAGE -> ResponseBehavior.sendMessage(this, message);
                    case CLOSE_SESSION -> ResponseBehavior.closeSession(this, message);
                    case GET_LAST_MESSAGES -> ResponseBehavior.getLastMessages(this, message);
                    default -> ResponseBehavior.error(this);
                }
            } catch (SocketException e) {
                break;
            }
        }

        close();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
