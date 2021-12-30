package com.company.messaging.messages;

import lombok.Data;
import lombok.NonNull;

@Data
public class CatMessage implements StompMessage {

    @NonNull
    String userName;

    @NonNull
    String recipient;
}
