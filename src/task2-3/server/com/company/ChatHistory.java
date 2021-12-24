package com.company;

import lombok.*;
import lombok.experimental.NonFinal;

import java.util.LinkedList;
import java.util.List;

@Data
public class ChatHistory {

    private static final int CHAT_HISTORY_SIZE = 10;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    List<MessageDTO> chatHistory = new LinkedList<>();

    public synchronized ChatHistory addMessage(MessageDTO message) {
        chatHistory.add(message);
        return this;
    }

    public synchronized List<MessageDTO> getLastMessages(MessageDTO askMessage) {
//                        .filter(message -> !message.getSender().equals(askMessage.getSender()))
        return chatHistory.stream()
                .filter(message -> message.getRecipient().equals("@all") ||
                        message.getRecipient().equals(askMessage.getSender()))
                .limit(CHAT_HISTORY_SIZE)
                .toList();
    }

    /**
     * @return new messages from parameter
     */
    public synchronized List<MessageDTO> update(List<MessageDTO> updatedMessages) {
        List<MessageDTO> newMessages = updatedMessages.stream()
                .filter(m -> !chatHistory.contains(m))
                .toList();
        chatHistory = updatedMessages;
        return newMessages;
    }
}
