package ru.butakov.bash_im_bot.bot.handlers.admin;

import org.junit.jupiter.api.Test;
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
import ru.butakov.bash_im_bot.service.StripService;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdateStripItemDbHandlerTest {
    @MockBean
    StateService stateService;
    @MockBean
    StripService stripService;
    @MockBean
    UnknownCommandHandler unknownCommandHandler;
    @MockBean
    SendMessageFormat sendMessageFormat;
    @Value("${bot.command.update.stripdb}")
    String command;
    @Value("${bot.admin.chatid}")
    long adminId;
    @Value("${reply.command.update.stripdb.end}")
    String answerText;
    @Value("${bash.im.strips.updatedb.firstperiod}")
    int firstPeriod;
    @Autowired
    UpdateStripItemDbHandler updateStripItemDbHandler;

    @Test
    void handleTestSucceessfulTest() {

        long chatId = adminId;
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText("text");

        SendMessage expectedSendMessage = new SendMessage(String.valueOf(chatId), answerText);
        Mockito.when(sendMessageFormat.getSendMessageBaseFormat(Mockito.eq(chatId), Mockito.anyString())).thenReturn(expectedSendMessage);

        SendMessage actualSendMessage = updateStripItemDbHandler.handle(message);

        assertEquals(String.valueOf(chatId), actualSendMessage.getChatId());
        assertEquals(answerText, actualSendMessage.getText());
        Mockito.verify(stateService, Mockito.times(1)).clearStripItems();
        Mockito.verify(stripService, Mockito.times(1)).generateStripDb(Mockito.eq(firstPeriod), Mockito.anyInt());
    }

    @Test
    void handleTestAccessDeniedTest() {

        long chatId = 12345L;
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText("text");

        String expectedText = "text";
        SendMessage expectedSendMessage = new SendMessage(String.valueOf(chatId), expectedText);
        Mockito.when(unknownCommandHandler.handle(Mockito.any())).thenReturn(expectedSendMessage);

        SendMessage actualSendMessage = updateStripItemDbHandler.handle(message);

        assertEquals(String.valueOf(chatId), actualSendMessage.getChatId());
        assertEquals(expectedText, actualSendMessage.getText());
        Mockito.verifyNoInteractions(stateService, stripService);
    }

    @Test
    void getAnswerTextTest() {
        assertEquals(answerText, updateStripItemDbHandler.getAnswerText());
    }

    @Test
    void getCommandStringTest() {
        assertEquals(command, updateStripItemDbHandler.getCommandString());
    }
}