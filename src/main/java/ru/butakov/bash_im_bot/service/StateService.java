package ru.butakov.bash_im_bot.service;

import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.Set;

public interface StateService {
    int updateMaxQuoteNumber(int number);

    int getMaxQuoteNumber();

    void addUser(long chatId);

    void removeUser(long chatId);

    Set<Long> getUserSet();

    int getUserCount();

    Set<StripItem> getStripItemSet();

    int getStripCount();

    boolean addStripItemToDb(StripItem item);

    void clearStripItems();
}
