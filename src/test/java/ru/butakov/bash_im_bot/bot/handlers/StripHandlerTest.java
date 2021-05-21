package ru.butakov.bash_im_bot.bot.handlers;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.butakov.bash_im_bot.bot.SendMessageFormat;
import ru.butakov.bash_im_bot.service.StripService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StripHandlerTest {
    @MockBean
    StripService stripService;
    @MockBean
    SendMessageFormat sendMessageFormat;
    @Value("${bot.button.strip}")
    String command;
    @Autowired
    StripHandler stripHandler;

    @Test
    void handleTest() {
        long chatId = 1234L;
        String text = "text";
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(text);

        String expectedText = "strip text";
        SendMessage actualSendMessage = new SendMessage(String.valueOf(chatId), expectedText);
        Mockito.when(sendMessageFormat.getSendMessageBaseFormat(chatId, expectedText)).thenReturn(actualSendMessage);

        assertEquals(String.valueOf(chatId), actualSendMessage.getChatId());
        assertEquals(expectedText, actualSendMessage.getText());
    }

    @Test
    void getAnswerTextTest() {
        String expectedText = "strip text";
        Mockito.when(stripService.getRandomStrip()).thenReturn(expectedText);
        String actualText = stripHandler.getAnswerText();

        Mockito.verify(stripService, Mockito.times(1)).getRandomStrip();
        assertEquals(expectedText, actualText);
    }

    @Test
    void getCommandStringTest() {
        assertEquals(command, stripHandler.getCommandString());
    }
}