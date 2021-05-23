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
import ru.butakov.bash_im_bot.bot.SendMessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UnknownCommandHandlerTest extends AbstractTest {
    @MockBean
    SendMessageFormat sendMessageFormat;
    @Value("${reply.unknown}")
    String answerText;
    @Autowired
    UnknownCommandHandler unknownCommandHandler;

    @Test
    void getAnswerText() {
        assertEquals(answerText, unknownCommandHandler.getAnswerText());
    }

    @Test
    void getCommandString() {
        assertNull(unknownCommandHandler.getCommandString());
    }

    @Test
    void handleTest() {
        long chatId = 1234L;
        String text = "text";
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(text);

        SendMessage expectedSendMessage = new SendMessage(String.valueOf(chatId), answerText);
        Mockito.when(sendMessageFormat.getSendMessageBaseFormat(chatId, answerText)).thenReturn(expectedSendMessage);
        SendMessage actualSendMessage = unknownCommandHandler.handle(message);

        Mockito.verify(sendMessageFormat, Mockito.times(1)).getSendMessageBaseFormat(chatId, answerText);
        assertEquals(expectedSendMessage, actualSendMessage);

    }
}