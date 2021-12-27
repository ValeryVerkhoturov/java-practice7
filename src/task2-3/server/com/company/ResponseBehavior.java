package com.company;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseBehavior {

    @SneakyThrows
    public void greetings(ServerSession serverSession) {
        serverSession.getObjectOutputStream()
                .writeObject(MessageDTO.builder()
                        .message("Добро пожаловать на сервер Шизофрения. Введите имя.")
                        .command(Command.OK)
                        .build());
    }

    public void setName(ServerSession serverSession, MessageDTO messageDTO) {
        serverSession.setName(messageDTO.getMessage());
        serverSession.getChatHistory().addMessage(MessageDTO.builder()
                .sender("@server")
                .recipient(messageDTO.getSender())
                .message("Имя установлено.")
                .command(Command.OK)
                .build());
    }

    public void sendCat(ServerSession serverSession, MessageDTO messageDTO) {
        serverSession.getChatHistory()
                .addMessage(messageDTO)
                .addMessage(MessageDTO.builder()
                        .sender("@server")
                        .recipient(messageDTO.getSender())
                        .message("Кошка брошена в " + messageDTO.getRecipient())
                        .command(Command.OK)
                        .build());
    }

    public void sendMessage(ServerSession serverSession, MessageDTO messageDTO) {
        serverSession.getChatHistory()
                .addMessage(messageDTO)
                .addMessage(MessageDTO.builder()
                        .sender("@server")
                        .recipient(messageDTO.getSender())
                        .message("Сообщение отправлено")
                        .command(Command.OK)
                        .build());
    }

    public void closeSession(ServerSession serverSession, MessageDTO messageDTO) {
        serverSession.setSessionLoop(false);
        serverSession.getChatHistory().addMessage(MessageDTO.builder()
                .sender("@server")
                .recipient(messageDTO.getSender())
                .message("Сессия закрыта")
                .command(Command.CLOSE_SESSION)
                .build());
    }

    @SneakyThrows
    public void getLastMessages(ServerSession serverSession, MessageDTO messageDTO) {
        serverSession.getObjectOutputStream()
                .writeObject(MessageDTO.builder()
                        .sender("@server")
                        .recipient(messageDTO.getSender())
                        .messages(serverSession.getChatHistory().getLastMessages(messageDTO))
                        .command(Command.SEND_LAST_MESSAGES)
                        .build());
    }

    @SneakyThrows
    public void error(ServerSession serverSession) {
        serverSession.getObjectOutputStream().writeObject(MessageDTO.builder()
                .command(Command.ERROR)
                .build());
    }
}
