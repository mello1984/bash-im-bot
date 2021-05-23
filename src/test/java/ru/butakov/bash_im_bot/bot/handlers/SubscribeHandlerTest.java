package ru.butakov.bash_im_bot.bot.handlers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.AbstractTest;
import ru.butakov.bash_im_bot.service.StateService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscribeHandlerTest extends AbstractTest {
    @MockBean
    StateService stateService;
    @Value("${reply.subscribe}")
    String answerText;
    @Value("${bot.button.subscribe}")
    String command;
    @Autowired
    SubscribeHandler subscribeHandler;

    @Test
    void handleTest() {
        long chatId = 1234L;
        String text = "text";
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(text);

        SendMessage actualSendMessage = subscribeHandler.handle(message);

        Mockito.verify(stateService, Mockito.times(1)).addUser(chatId);
        assertEquals(String.valueOf(chatId), actualSendMessage.getChatId());
        assertEquals(answerText, actualSendMessage.getText());
    }

    @Test
    void getAnswerTextTest() {
        assertEquals(answerText, subscribeHandler.getAnswerText());
    }

    @Test
    void getCommandStringTest() {
        assertEquals(command, subscribeHandler.getCommandString());
    }
}