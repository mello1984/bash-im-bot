package ru.butakov.bash_im_bot.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.butakov.bash_im_bot.AbstractTest;

class MessageSenderServiceImplTest extends AbstractTest {
    @Autowired
    MessageSenderServiceImpl messageSenderService;

    @Test
    void sendUpdateFromQueueTest() throws Exception {
        BotApiMethod<?> sendMessage = new SendMessage();
        Mockito.when(bashBot.execute(sendMessage)).thenReturn(null);

        messageSenderService.offerBotApiMethodToQueue(sendMessage);
        Thread.sleep(500);

        Mockito.verify(bashBot, Mockito.times(1)).execute(sendMessage);
    }

    @Test
    void sendUpdateFromQueueExceptionTest() throws Exception {

        SendMessage sendMessage = new SendMessage();
        Mockito.when(bashBot.execute(sendMessage))
                .thenThrow(TelegramApiException.class)
                .thenReturn(null);

        messageSenderService.offerBotApiMethodToQueue(sendMessage);
        Thread.sleep(5000);

        Mockito.verify(bashBot, Mockito.times(2)).execute(sendMessage);

    }
}