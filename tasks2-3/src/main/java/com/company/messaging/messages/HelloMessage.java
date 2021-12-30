package com.company.messaging.messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class HelloMessage implements StompMessage {

    @NonNull
    String userName;
}
