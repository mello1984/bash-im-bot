package ru.butakov.bash_im_bot.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.bash_im_bot.bot.handlers.HandlerManager;
import ru.butakov.bash_im_bot.service.MessageSenderService;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TelegramFacadeImpl implements TelegramFacade {
    @Autowired
    HandlerManager handlerManager;
    @Autowired
    MessageSenderService messageSenderService;

    @Override
    public void handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User: {}, with text: {}", message.getChatId(), message.getText());
            replyMessage = handlerManager.handleInputMessage(message);
        }

        if (replyMessage != null) messageSenderService.offerBotApiMethodToQueue(replyMessage);
    }
}
