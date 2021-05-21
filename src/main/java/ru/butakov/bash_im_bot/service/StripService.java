package ru.butakov.bash_im_bot.service;

public interface StripService {
    String getRandomStrip();

    void generateStripDb();

    void generateStripDb(int firstPeriod, int lastPeriod);
}
