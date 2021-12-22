package com.company.task1.ClientSide;

import lombok.*;
import lombok.experimental.NonFinal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Scanner;

@Value
@AllArgsConstructor(access = AccessLevel.NONE)
@RequiredArgsConstructor
public class ClientController implements Runnable {

    String host;

    int port;

    @NonFinal
    DataInputStream dataInputStream;

    @NonFinal
    DataOutputStream dataOutputStream;

    /**
     * Contract:<br>
     * "0" : String - message from server "Сессия завершена." : String and close session on server;<br>
     * wrong password : String - message from server "Неправильный пароль." : String<br>
     * correct password : String - messages from server:<br>
     *      "Передача файла..." : String<br>
     *      fileBytes.length : int<br>
     *      fileBytes : byte[]<br>
     *      "Файл передан." : String<br>
     */
    @SneakyThrows
    @Override
    public void run() {
        @Cleanup Socket client = new Socket(host, port);
        dataInputStream = new DataInputStream(client.getInputStream());
        dataOutputStream = new DataOutputStream(client.getOutputStream());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            final String message = dataInputStream.readUTF();
            System.out.println(message);
            if (message.equals("Передача файла..."))
                readFile();
            else if (message.equals("Сессия завершена."))
                break;

            dataOutputStream.writeUTF(scanner.nextLine());
        }

        closeStreams();
        client.close();
    }

    @SneakyThrows
    private void readFile() {
        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/com/company/task1/ClientSide/file.txt");
        fileOutputStream.write(dataInputStream.readNBytes(dataInputStream.readInt()));
        fileOutputStream.flush();
        System.out.println(dataInputStream.readUTF());
    }

    @SneakyThrows
    private void closeStreams() {
        dataInputStream.close();
        dataOutputStream.close();
    }
}
