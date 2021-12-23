package com.company;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Synchronized;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;

@Value
public class ChatHistory {

    static final int CHAT_HISTORY_SIZE = 10;

    @Getter(value = AccessLevel.NONE)
    List<MessageDTO> chatHistory = new LinkedList<>();

    public synchronized ChatHistory addMessage(MessageDTO message) {
        chatHistory.add(message);
        return this;
    }

    public synchronized List<MessageDTO> getLastMessages(MessageDTO askMessage) {
        return chatHistory.stream()
                .filter(message -> message.getRecipient().equals("@all") ||
                        message.getRecipient().equals(askMessage.getSender()))
                .filter(message -> !message.getSender().equals(askMessage.getSender()))
                .limit(CHAT_HISTORY_SIZE)
                .toList();
    }
}
