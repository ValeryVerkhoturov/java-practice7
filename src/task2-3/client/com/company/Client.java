package com.company;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Client implements Runnable{

    public static void main(String[] args) {
        new Client().run();
    }

    @Override
    public void run() {

    }
}
