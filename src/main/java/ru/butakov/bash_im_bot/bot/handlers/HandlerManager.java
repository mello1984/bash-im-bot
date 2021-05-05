package ru.butakov.bash_im_bot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandlerManager {
    final Map<String, InputMessageHandler> handlersMap = new HashMap<>();

    @Autowired
    @Qualifier("unknownCommandHandler")
    InputMessageHandler defaultHandler;

    public HandlerManager(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> handlersMap.put(handler.getCommandString(), handler));
    }

    public SendMessage handleInputMessage(Message message) {
        return handlersMap.getOrDefault(message.getText(), defaultHandler).handle(message);
    }
}
