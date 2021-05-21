package ru.butakov.bash_im_bot.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface MessageSenderService {
    boolean offerBotApiMethodToQueue(BotApiMethod<?> botApiMethod);
}
