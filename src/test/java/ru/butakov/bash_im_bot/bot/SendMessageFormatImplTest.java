package ru.butakov.bash_im_bot.bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.butakov.bash_im_bot.AbstractTest;

import static org.junit.jupiter.api.Assertions.*;

class SendMessageFormatImplTest extends AbstractTest {
    @Autowired
    SendMessageFormatImpl sendMessageFormat;

    @Test
    void getSendMessageBaseFormatIdTest() {
        long chatId = 128L;
        SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(chatId);

        assertEquals(String.valueOf(chatId), sendMessage.getChatId());
        assertEquals("HTML", sendMessage.getParseMode());
        assertTrue(sendMessage.getDisableWebPagePreview());
        assertTrue(sendMessage.getDisableWebPagePreview());
    }

    @Test
    void getSendMessageBaseFormatIdTextTest() {
        long chatId = 128L;
        String text = "text";
        SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(chatId, text);

        assertEquals(String.valueOf(chatId), sendMessage.getChatId());
        assertEquals(text, sendMessage.getText());
        assertTrue(sendMessage.getDisableWebPagePreview());
    }

    @Test
    void getSendMessageBaseFormatIdTextWpfTest() {
        long chatId = 128L;
        String text = "text";
        SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(chatId, text);

        assertEquals(String.valueOf(chatId), sendMessage.getChatId());
        assertEquals(text, sendMessage.getText());
        assertFalse(sendMessage.getDisableWebPagePreview());
    }
}