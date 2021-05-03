package ru.butakov.bash_im_bot.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@Getter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BashBot extends TelegramLongPollingBot {
    final String botUsername;
    final String botToken;
    @Autowired // Not in Constructor because cycle bean exception
    TelegramFacade telegramFacade;

    public BashBot(@Value("${bot.name:}") String botName,
                   @Value("${bot.token:}") String botToken
) {
        if (botName.isEmpty()) botName = System.getenv("bot_name");
        if (botToken.isEmpty()) botToken = System.getenv("bot_token");
        this.botUsername = botName;
        this.botToken = botToken;

        while (true) {
            if (registerBot()) break;
        }
    }

    private boolean registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            log.info("Register bot successful");
            return true;
        } catch (TelegramApiException e) {
            log.warn("Exception on register bot", e);
            return false;
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramFacade.handleUpdate(update);
    }
}
