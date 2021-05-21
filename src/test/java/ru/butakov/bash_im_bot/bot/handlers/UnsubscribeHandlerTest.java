package ru.butakov.bash_im_bot.bot.handlers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.service.StateService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnsubscribeHandlerTest {
    @MockBean
    StateService stateService;
    @Value("${reply.unsubscribe}")
    String answerText;
    @Value("${bot.button.unsubscribe}")
    String command;
    @Autowired
    UnsubscribeHandler unsubscribeHandler;

    @Test
    void handleTest() {
        long chatId = 1234L;
        String text = "text";
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(text);

        SendMessage expectedSendMessage = new SendMessage(String.valueOf(chatId), answerText);
        SendMessage actualSendMessage = unsubscribeHandler.handle(message);

        Mockito.verify(stateService, Mockito.times(1)).removeUser(chatId);
        assertEquals(expectedSendMessage.getChatId(), actualSendMessage.getChatId());
        assertEquals(expectedSendMessage.getText(), actualSendMessage.getText());
    }

    @Test
    void getAnswerTextTest() {
        assertEquals(answerText, unsubscribeHandler.getAnswerText());
    }

    @Test
    void getCommandStringTest() {
        assertEquals(command, unsubscribeHandler.getCommandString());
    }
}