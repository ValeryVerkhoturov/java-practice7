package com.company.messaging;

import com.company.messaging.history.ChatHistory;
import com.company.messaging.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatHistoryController {

    ChatHistory chatHistory;

    @Autowired
    public ChatHistoryController() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("com.company.messaging.history");
        context.refresh();
        chatHistory = context.getBean("chatHistory", ChatHistory.class);
    }

    @GetMapping("/lastMessages")
    public List<Message> latMessages() {
        return new ArrayList<>(chatHistory.getLastMessages());
    }
}
