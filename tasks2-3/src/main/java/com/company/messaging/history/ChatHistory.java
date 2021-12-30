package com.company.messaging.history;

import com.company.messaging.messages.Message;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatHistory {

    int lastMessagesCapacity = 10;

    @Getter
    Queue<Message> lastMessages = new ArrayBlockingQueue<>(lastMessagesCapacity);

    public void add(Message message) {
        if (lastMessages.size() >= lastMessagesCapacity)
            lastMessages.poll();
        lastMessages.add(message);
    }
}
