package com.company;

import lombok.Cleanup;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@Value @NonFinal
public class Cat implements Serializable {

    String name;

    Duration age;

    static List<String> names;

    static {
        try {
            @Cleanup FileReader fileReader = new FileReader("src/task2-3/resources/task2-3.properties");
            Properties properties = new Properties();
            properties.load(fileReader);

            @Cleanup BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(properties.getProperty("resources.path.catNames")));

            loadCatNames(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadCatNames(BufferedReader bufferedReader) {
        names = bufferedReader.lines().toList();
    }

    private static String getRandomName() {
        return names.get(ThreadLocalRandom.current().nextInt(names.size()));
    }

    private static Duration getRandomAge() {
        final long maxAgeInMilliseconds = 1262304000000L; // 01/01/2010 00:00:00
        final long minAgeInMilliseconds = 1609459200000L; // 01/01/2021 00:00:00
        return Duration.ofMillis(ThreadLocalRandom.current().nextLong(maxAgeInMilliseconds, minAgeInMilliseconds));
    }

    public static Cat newRandomInstance() {
        return new Cat(getRandomName(), getRandomAge());
    }
}
