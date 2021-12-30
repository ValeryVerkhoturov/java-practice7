package com.company;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public final class ServerPaths {
    static String FILE = "task1/src/main/server/com/company/file.txt";
}
