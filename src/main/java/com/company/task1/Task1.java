package com.company.task1;

import com.company.task1.ClientSide.ClientController;
import com.company.task1.ServerSide.ServerController;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

final class Task1 {

    private static final Properties properties = new Properties();

    @SneakyThrows
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("src/main/resources/task1.properties");
        properties.load(fileReader);
        fileReader.close();

        new Thread(new ServerController(Integer.parseInt(properties.getProperty("port")))).start();
        TimeUnit.SECONDS.sleep(1);
        new ClientController(properties.getProperty("hostname"), Integer.parseInt(properties.getProperty("port"))).run();


    }

}
