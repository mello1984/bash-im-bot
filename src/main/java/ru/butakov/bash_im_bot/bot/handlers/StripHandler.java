package ru.butakov.bash_im_bot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.service.StripService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripHandler extends AbstractHandler {
    @Autowired
    StripService stripService;
    @Value("${bot.button.strip}")
    String command;

    @Override
    public SendMessage handle(Message message) {
        return sendMessageFormat.getSendMessageBaseFormat(message.getChatId(), getAnswerText(), true);
    }

    @Override
    protected String getAnswerText() {
       return stripService.getRandomStrip();
    }

    @Override
    public String getCommandString() {
        return command;
    }
}
