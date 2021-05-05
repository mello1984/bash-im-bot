package ru.butakov.bash_im_bot.bot.handlers.admin;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.bot.handlers.AbstractHandler;
import ru.butakov.bash_im_bot.bot.handlers.UnknownCommandHandler;
import ru.butakov.bash_im_bot.service.StateService;

import java.text.MessageFormat;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsHandler extends AbstractHandler {
    @Autowired
    StateService stateService;
    @Autowired
    UnknownCommandHandler unknownCommandHandler;
    @Value("${reply.command.statistics}")
    String text;
    @Value("${bot.command.statistics}")
    String command;
    @Value("${bot.admin.chatid}")
    long adminId;
    String answerText;

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        if (chatId != adminId) return unknownCommandHandler.handle(message);
        answerText = MessageFormat.format(text, stateService.getUserCount(), stateService.getMaxQuoteNumber(), stateService.getStripCount());
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
