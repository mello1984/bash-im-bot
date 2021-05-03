package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.Random;
import java.util.Set;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StateService stateService;
    Random random = new Random();

    public String getRandomStrip() {
        Set<Integer> strips = stateService.getStripsSet();
        int rnd = random.nextInt(strips.size() - 1);
        int number = strips.stream().skip(rnd).findFirst().get();
        System.out.println(strips.size() + ":" + rnd + ":" + number);
        StripItem stripItem = new StripItem(number);
        return stripItem.getTextMessage();
    }
}
