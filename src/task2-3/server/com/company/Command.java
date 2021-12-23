package com.company;

import java.io.Serializable;

public enum Command {
    // From client
    NULL,
    SET_NAME,
    GET_LAST_MESSAGES,
    SEND_MESSAGE,
    SEND_CAT,

    // From server
    SEND_LAST_MESSAGES,
    OK,
    ERROR,

    // Both
    CLOSE_SESSION
}
