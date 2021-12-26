package com.company;

import lombok.*;
import lombok.experimental.NonFinal;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;
import java.util.Scanner;

@Value
@AllArgsConstructor(access = AccessLevel.NONE)
@RequiredArgsConstructor
public class ClientController implements Runnable, Closeable {

    String host;

    int port;

    @NonFinal
    Socket client;

    @NonFinal
    DataInputStream dataInputStream;

    @NonFinal
    DataOutputStream dataOutputStream;

    public static void main(String[] args) throws IOException {
        @Cleanup FileReader fileReader = new FileReader("src/task1/resources/task1.properties");
        Properties properties = new Properties();
        properties.load(fileReader);

        new ClientController(properties.getProperty("hostname"), Integer.parseInt(properties.getProperty("port"))).run();
    }

    /**
     * Contract with server:<br>
     * connect to server - message from server "Вы подключились к серверу \"1 + 1 =\". 0 - Выход. Введите пароль."
     * send "0" : String - message from server "Сессия завершена." : String and close session on server;<br>
     * send wrong password : String - message from server "Неправильный пароль." : String<br>
     * send correct password : String - messages from server:<br>
     *      "Передача файла..." : String<br>
     *      fileBytes.length : int<br>
     *      fileBytes : byte[]<br>
     *      "Файл передан." : String<br>
     */
    @SneakyThrows
    @Override
    public void run() {
        try {
            client = new Socket(host, port);
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
                try {
                    dataOutputStream.writeUTF(scanner.nextLine());
                } catch (SocketException e) {
                    System.out.println("Нет соединения с сервером.");
                    break;
                }
            }

            close();
        } catch (ConnectException e) {
            System.out.println("Не запущен сервер");
        }
    }

    @SneakyThrows
    private void readFile() {
        @Cleanup FileOutputStream fileOutputStream = new FileOutputStream(ClientPaths.FILE);
        fileOutputStream.write(dataInputStream.readNBytes(dataInputStream.readInt()));
        fileOutputStream.flush();
        System.out.println(dataInputStream.readUTF());
    }

    @SneakyThrows
    public void close() {
        client.close();
    }
}
