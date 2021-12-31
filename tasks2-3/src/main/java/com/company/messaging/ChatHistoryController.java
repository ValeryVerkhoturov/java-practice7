package com.company.messaging;

import com.company.messaging.history.ChatHistory;
import com.company.messaging.messages.Message;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatHistoryController {

    @Autowired
    ChatHistory chatHistory;

    @GetMapping("/lastMessages")
    public List<Message> lastMessages() {
        return new ArrayList<>(chatHistory.getLastMessages());
    }
}
