package com.company.messaging;

import com.company.messaging.history.ChatHistory;
import com.company.messaging.messages.CatMessage;
import com.company.messaging.messages.HelloMessage;
import com.company.messaging.messages.Message;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessagingController {

	final String serverName = "Server";

	@Autowired
	ChatHistory chatHistory;

	@MessageMapping("/hello")
	@SendTo("/topic/messages")
	public Message greeting(HelloMessage helloMessage) {
		Message message = new Message(serverName, helloMessage.getUserName() + " присоединился.");
        chatHistory.add(message);
        return message;
	}

	@MessageMapping("/message")
	@SendTo("/topic/messages")
	public Message messaging(Message message) {
		chatHistory.add(message);
		return message;
	}

	@MessageMapping("/cat")
	@SendTo("/topic/messages")
	public Message catSending(CatMessage catMessage) {
		Message message = new Message(serverName, catMessage.getUserName() + " бросил кошку в " + catMessage.getRecipient());
        chatHistory.add(message);
        return message;
	}
}
