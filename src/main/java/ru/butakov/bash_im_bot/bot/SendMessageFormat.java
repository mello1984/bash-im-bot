package ru.butakov.bash_im_bot.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public interface SendMessageFormat {
    SendMessage getSendMessageBaseFormat(long chatId);

    SendMessage getSendMessageBaseFormat(long chatId, String text);

    SendMessage getSendMessageBaseFormat(long chatId, String text, boolean enableWebPagePreview);

    void setButtons(SendMessage sendMessage, List<List<String>> buttons);

    void setButtons(SendMessage sendMessage, String... buttons);
}
