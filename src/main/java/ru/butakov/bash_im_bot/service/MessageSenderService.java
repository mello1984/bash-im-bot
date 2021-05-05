package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.butakov.bash_im_bot.bot.BashBot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageSenderService {
    @Autowired
    BashBot bashBot;
    final BlockingQueue<BotApiMethod<?>> updateBlockingQueue = new LinkedBlockingQueue<>();

    @Scheduled(fixedDelayString = "${bot.sendmessage.updateperiod}")
    private void sendUpdateFromQueue() throws InterruptedException {
        BotApiMethod<?> botApiMethod = updateBlockingQueue.take();
        try {
            bashBot.execute(botApiMethod);
            if (botApiMethod instanceof SendMessage) {
                SendMessage sendMessage = (SendMessage) botApiMethod;
                log.info("Send message to User: {}", sendMessage.getChatId());
            } else log.info("BotApiMethod executed: {}", botApiMethod.toString());
        } catch (TelegramApiException e) {
            log.warn("Exception with executing botApiMethod: {}", botApiMethod.toString(), e);
            Thread.sleep(3000);
        }
    }

    public boolean offerBotApiMethodToQueue(BotApiMethod<?> botApiMethod) {
        return updateBlockingQueue.offer(botApiMethod);
    }
}
