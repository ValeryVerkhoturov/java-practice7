package com.company;

import lombok.Getter;
import lombok.Synchronized;
import lombok.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class ChatHistory {

    static final int CHAT_HISTORY_SIZE = 10;

    @Getter(onMethod_ = {@Synchronized})
    List<MessageDTO> chatHistory = new LinkedList<>();

    public synchronized void addMessage(MessageDTO message) {
        chatHistory.add(message);
    }

    public synchronized List<MessageDTO> getLastMessages(MessageDTO messageDTO) {
        return chatHistory.stream()
                .filter(message -> message.getRecipient().equals("@all") ||
                        message.getRecipient().equals(messageDTO.getRecipient()))
                .filter(message -> !message.getSender().equals(messageDTO.getSender()))
                .limit(CHAT_HISTORY_SIZE)
                .toList();
    }
}
