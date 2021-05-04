package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.Random;
import java.util.Set;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripService {
    @Autowired
    StateService stateService;
    final Random random = new Random();

    public String getRandomStrip() {
        Set<StripItem> strips = stateService.getStripItemSet();
        int rnd = random.nextInt(strips.size() - 1);
        StripItem stripItem = strips.stream().skip(rnd).findFirst().get();
        return stripItem.getTextMessage();
    }
}
