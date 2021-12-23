package com.company;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

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

    Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;

    static int MAX_CATS_PER_SESSION = 10;
    static int MIN_CATS_PER_SESSION = 5;

    List<Cat> cats;

    @SneakyThrows
    public ServerSession(Socket socket) {
        this.socket = socket;
        dataInputStream = new DataInputStream(this.socket.getInputStream());
        dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());

        cats = new ArrayList<>(
                Stream.generate(Cat::newRandomInstance)
                        .limit(ThreadLocalRandom.current().nextInt(MIN_CATS_PER_SESSION, MAX_CATS_PER_SESSION))
                        .toList());
    }

    @Override
    public void run() {

    }
}
