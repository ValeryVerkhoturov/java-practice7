package com.company.messaging;

import com.company.messaging.history.ChatHistory;
import com.company.messaging.messages.CatMessage;
import com.company.messaging.messages.HelloMessage;
import com.company.messaging.messages.Message;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessagingController {

	String serverName = "Server";

	ChatHistory chatHistory;

	@Autowired
	public MessagingController() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.company.messaging.history");
		context.refresh();
		chatHistory = context.getBean("chatHistory", ChatHistory.class);
	}

	@MessageMapping("/hello")
	@SendTo("/topic/messages")
	public Message greeting(HelloMessage helloMessage) {
		return new Message(serverName, helloMessage.getUserName() + " присоединился.");
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
		return new Message(serverName, catMessage.getUserName() + "бросил кошку в " + catMessage.getRecipient());
	}
}
