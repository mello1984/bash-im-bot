package ru.butakov.bash_im_bot.bot.handlers.admin;

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
import ru.butakov.bash_im_bot.bot.handlers.UnknownCommandHandler;
import ru.butakov.bash_im_bot.service.StateService;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatisticsHandlerTest {
    @MockBean
    StateService stateService;
    @MockBean
    UnknownCommandHandler unknownCommandHandler;
    @MockBean
    SendMessageFormat sendMessageFormat;
    @Value("${reply.command.statistics}")
    String text;
    @Value("${bot.command.statistics}")
    String command;
    @Value("${bot.admin.chatid}")
    long adminId;
    @Autowired
    StatisticsHandler statisticsHandler;

    @Test
    void handleSuccessfulTest() {
        long chatId = adminId;
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText("text");

        Mockito.when(stateService.getUserCount()).thenReturn(10);
        Mockito.when(stateService.getMaxQuoteNumber()).thenReturn(15);
        Mockito.when(stateService.getStripCount()).thenReturn(20);
        String expectedText = MessageFormat.format(command, 10, 15, 20);

        SendMessage expectedSendMessage = new SendMessage(String.valueOf(chatId), expectedText);
        Mockito.when(sendMessageFormat.getSendMessageBaseFormat(Mockito.eq(chatId), Mockito.anyString())).thenReturn(expectedSendMessage);

        SendMessage actualSendMessage = statisticsHandler.handle(message);

        assertEquals(String.valueOf(chatId), actualSendMessage.getChatId());
        assertEquals(expectedText, actualSendMessage.getText());
        Mockito.verify(sendMessageFormat, Mockito.times(1)).getSendMessageBaseFormat(Mockito.anyLong(), Mockito.anyString());
    }

    @Test
    void handleAccessDeniedTest() {
        long chatId = 12345L;
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(command);

        String expectedText = "text";
        SendMessage expectedSendMessage = new SendMessage(String.valueOf(chatId), expectedText);
        Mockito.when(unknownCommandHandler.handle(Mockito.any())).thenReturn(expectedSendMessage);

        SendMessage actualSendMessage = statisticsHandler.handle(message);

        assertEquals(String.valueOf(chatId), actualSendMessage.getChatId());
        assertEquals(expectedText, actualSendMessage.getText());
        Mockito.verify(unknownCommandHandler, Mockito.times(1)).handle(message);
    }

    @Test
    void getCommandStringTest() {
        assertEquals(command, statisticsHandler.getCommandString());
    }
}