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
import ru.butakov.bash_im_bot.service.StripService;

import java.time.LocalDate;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStripItemDbHandler extends AbstractHandler {
    @Autowired
    StripService stripService;
    @Autowired
    StateService stateService;
    @Autowired
    UnknownCommandHandler unknownCommandHandler;
    @Value("${bot.command.update.stripdb}")
    String command;
    @Value("${bot.admin.chatid}")
    long adminId;
    @Value("${reply.command.update.stripdb.end}")
    String answerText;
    @Value("${bash.im.strips.updatedb.firstperiod}")
    int firstPeriod;

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        if (chatId != adminId) return unknownCommandHandler.handle(message);

        log.info("begin update strip db");
        stateService.clearStripItems();

        log.info("strip db clear");
        LocalDate ld = LocalDate.now();
        int lastPeriod = ld.getYear() * 100 + ld.getMonthValue();
        stripService.generateStripDb(firstPeriod, lastPeriod);

        log.info("end update strip db");
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
