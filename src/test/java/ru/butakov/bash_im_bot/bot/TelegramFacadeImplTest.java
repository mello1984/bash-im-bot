package ru.butakov.bash_im_bot.bot;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.butakov.bash_im_bot.bot.handlers.HandlerManager;
import ru.butakov.bash_im_bot.service.MessageSenderService;

@SpringBootTest
class TelegramFacadeImplTest {
    @Autowired
    TelegramFacadeImpl telegramFacade;
    @MockBean
    HandlerManager handlerManager;
    @MockBean
    MessageSenderService messageSenderService;

    @Test
    void handleUpdateSuccessfulTest() {
        Message message = new Message();
        message.setChat(new Chat(1234L, "private"));
        message.setText("test-text-1");

        Update update = new Update();
        update.setMessage(message);

        SendMessage resultSendMessage = new SendMessage(String.valueOf(message.getChatId()), message.getText());

        Mockito.when(handlerManager.handleInputMessage(message)).thenReturn(resultSendMessage);

        telegramFacade.handleUpdate(update);
        Mockito.verify(handlerManager, Mockito.times(1)).handleInputMessage(message);
        Mockito.verify(messageSenderService, Mockito.times(1)).offerBotApiMethodToQueue(resultSendMessage);
        Mockito.verify(handlerManager, Mockito.times(1)).handleInputMessage(Mockito.any());
        Mockito.verify(messageSenderService, Mockito.times(1)).offerBotApiMethodToQueue(Mockito.any());
    }

    @Test
    void handleUpdateNullMessageTest() {
        Update update = new Update();
        update.setMessage(null);

        telegramFacade.handleUpdate(update);
        Mockito.verify(handlerManager, Mockito.never()).handleInputMessage(Mockito.any());
        Mockito.verify(messageSenderService, Mockito.never()).offerBotApiMethodToQueue(Mockito.any());
    }

    @Test
    void handleUpdateEmptyMessageTextTest() {
        Message message = new Message();
        message.setChat(new Chat(1234L, "private"));
        message.setText("");

        Update update = new Update();
        update.setMessage(message);

        SendMessage resultSendMessage = new SendMessage(String.valueOf(message.getChatId()), message.getText());

        Mockito.when(handlerManager.handleInputMessage(message)).thenReturn(resultSendMessage);

        telegramFacade.handleUpdate(update);
        Mockito.verify(handlerManager, Mockito.never()).handleInputMessage(message);
        Mockito.verify(messageSenderService, Mockito.never()).offerBotApiMethodToQueue(resultSendMessage);
    }

    @Test
    void handleUpdateNullReplyMessageReturnTest() {
        Message message = new Message();
        message.setChat(new Chat(1234L, "private"));
        message.setText("test-text-1");

        Update update = new Update();
        update.setMessage(message);

        telegramFacade.handleUpdate(update);
        Mockito.verify(handlerManager, Mockito.times(1)).handleInputMessage(message);
        Mockito.verify(handlerManager, Mockito.times(1)).handleInputMessage(Mockito.any());
        Mockito.verify(messageSenderService, Mockito.never()).offerBotApiMethodToQueue(Mockito.any());
    }
}