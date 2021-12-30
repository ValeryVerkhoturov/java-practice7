package com.company.messaging.messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
public class Message implements StompMessage {

    @NonNull
    String userName;

    @NonNull
    String content;
}
