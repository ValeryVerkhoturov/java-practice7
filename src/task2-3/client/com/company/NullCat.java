package com.company;

import java.io.Serializable;
import java.time.Duration;

public class NullCat extends Cat implements Serializable {

    public NullCat() {
        super("Unnamed", Duration.ZERO);
    }
}
