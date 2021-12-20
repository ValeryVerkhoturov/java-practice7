package com.company.task1.ServerSide;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.Value;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

@Value
public class Session implements Runnable {

    Socket client;

    @SneakyThrows
    @Override
    public void run() {
        @Cleanup DataInputStream inputStream = new DataInputStream(client.getInputStream());
        @Cleanup DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        while (true)
            outputStream.writeUTF(inputStream.readUTF());
    }
}
