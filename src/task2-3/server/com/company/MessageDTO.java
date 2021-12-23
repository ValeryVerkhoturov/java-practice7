package com.company;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class MessageDTO {

    String sender;

    @Builder.Default
    String recipient = "@all";

    Command command;

    @Builder.Default
    String message = "";

    @Builder.Default
    Cat catToSend = new NullCat();

    @Builder.Default
    List<MessageDTO> messages = new ArrayList<>();
}
