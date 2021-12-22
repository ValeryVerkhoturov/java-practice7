package com.company;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public final class ServerPaths {
    static String FILE = "src/task1/server/com/company/file.txt";
}
