package ru.butakov.bash_im_bot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.service.StateService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscribeHandler extends AbstractHandler{
    @Autowired
    StateService stateService;
    @Value("${reply.subscribe}")
    String answerText;
    @Value("${bot.button.subscribe}")
    String command;

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        stateService.addUser(chatId);
        return super.handle(message);
    }

    @Override
    protected String getAnswerText() {
        return answerText;
    }

    @Override
    public String getCommandString() {
        return command;
    }
}
