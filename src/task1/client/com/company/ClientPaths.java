package com.company;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public final class ClientPaths {
    static String FILE = "src/task1/client/com/company/file.txt";
}
