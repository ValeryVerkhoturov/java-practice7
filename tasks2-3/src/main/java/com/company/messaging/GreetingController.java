package com.company.messaging;

import com.company.messaging.messages.HelloMessage;
import com.company.messaging.messages.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

	@MessageMapping("/hello")
	@SendTo("/topic/messages")
	public Message greeting(HelloMessage helloMessage) {
		return new Message("Server", helloMessage.getUserName() + " присоединился.");
	}

	@MessageMapping("/message")
	@SendTo("/topic/messages")
	public Message messaging(Message message) {
		return message;
	}

}
