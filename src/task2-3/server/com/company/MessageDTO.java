package com.company;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class MessageDTO implements Serializable {

    /**
     * Non null<br>
     * Example: "@all", "@server", "nickname"
     */
    @Builder.Default
    String sender = "Unnamed";

    /**
     * Non null<br>
     * Examples: "@all", "@server", "nickname"<br>
     */
    @Builder.Default
    String recipient = "Unnamed";

    /**
     * Null in message from server<br>
     * Non null in message from client<br>
     */
    @Builder.Default
    Command command = Command.NULL;

    /**
     * Nullable<br>
     * Non null nickname if command == Command.SET_NAME<br>
     */
    @Builder.Default
    String message = "No message";

    /** Nullable */
    @Builder.Default
    Cat catToSend = new NullCat();

    /**
     * Non null in message from server<br>
     * Null in message from client<br>
     */
    @Builder.Default
    List<MessageDTO> messages = new ArrayList<>();
}
