package ru.butakov.bash_im_bot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.bot.SendMessageFormat;

@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractHandler implements InputMessageHandler {
    @Autowired
    SendMessageFormat sendMessageFormat;
    String answerText = null;

    @Override
    public SendMessage handle(Message message) {
        return sendMessageFormat.getSendMessageBaseFormat(message.getChatId(), getAnswerText());
    }

    protected String getAnswerText() {
        return answerText;
    }
}
