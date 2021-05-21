package ru.butakov.bash_im_bot.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramFacade {
    void handleUpdate(Update update);
}
