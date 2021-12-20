package com.company.task1.ClientSide;

import lombok.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

@Value
public class ClientController implements Runnable {

    String host;

    int port;

    @SneakyThrows
    @Override
    public void run() {
        @Cleanup Socket client = new Socket(host, port);
        @Cleanup DataInputStream inputStream = new DataInputStream(client.getInputStream());
        @Cleanup DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            outputStream.writeUTF(scanner.nextLine());
            System.out.println(inputStream.readUTF());
        }
    }
}
