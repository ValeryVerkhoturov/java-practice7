package com.company;

import lombok.*;
import lombok.experimental.NonFinal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.net.Socket;

@Value
@AllArgsConstructor(access = AccessLevel.NONE)
@RequiredArgsConstructor
public class Session implements Runnable {

    Socket client;

    @NonFinal
    DataInputStream dataInputStream;

    @NonFinal
    DataOutputStream dataOutputStream;

    @SneakyThrows
    @Override
    public void run() {
        dataInputStream = new DataInputStream(client.getInputStream());
        dataOutputStream = new DataOutputStream(client.getOutputStream());

        dataOutputStream.writeUTF("Вы подключились к серверу \"1 + 1 =\". \n0 - Выход. \nВведите пароль.");
        final int password = 2;

        boolean loopStatement = true;
        while (loopStatement)
            try {
                int message = Integer.parseInt(dataInputStream.readUTF());
                switch (message) {
                    case password -> writeFile();
                    case 0 -> {
                        dataOutputStream.writeUTF("Сессия завершена.");
                        loopStatement = false;
                    }
                    default -> throw new WrongPasswordException();
                }
            } catch (NumberFormatException | WrongPasswordException e) {
                dataOutputStream.writeUTF("Неправильный пароль.");
            } catch (EOFException ignore) {}

        client.close();
    }

    @SneakyThrows
    private void writeFile() {
        @Cleanup FileInputStream fileInputStream = new FileInputStream(ServerPaths.FILE);
        byte[] data = fileInputStream.readAllBytes();
        dataOutputStream.writeUTF("Передача файла...");

        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data);

        dataOutputStream.writeUTF("Файл передан.");
    }
}
