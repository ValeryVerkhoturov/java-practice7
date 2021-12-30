package com.company;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public final class ClientPaths {
    static String FILE = "task1/src/main/client/com/company/file.txt";
}
