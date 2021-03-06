package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.butakov.bash_im_bot.bot.SendMessageFormat;
import ru.butakov.bash_im_bot.entity.rss.quote.QuoteItem;
import ru.butakov.bash_im_bot.entity.rss.quote.QuoteRSS;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;
import ru.butakov.bash_im_bot.entity.rss.strip.StripRSS;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdaterService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    SendMessageFormat sendMessageFormat;
    @Autowired
    MessageSenderService messageSenderService;
    @Autowired
    StateService stateService;
    @Value("${bash.im.quote.rss}")
    String rssQuoteLink;
    @Value("${bash.im.strip.rss}")
    String rssStripLink;

    @Scheduled(fixedDelayString = "${bash.im.updateperiod}")
    private void update() {
        updateQuotes();
        updateStrips();
    }

    private void updateStrips() {
        ResponseEntity<StripRSS> responseEntity = restTemplate.getForEntity(rssStripLink, StripRSS.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) return;

        StripRSS rss = responseEntity.getBody();
        List<StripItem> items = rss.getChannel().getItemList();
        Set<Long> subscribers = stateService.getUserIdSet();

        int count = 0;
        for (StripItem item : items) {
            if (stateService.addStripItemToDb(item)) {
                String text = item.getTextMessage();
                count++;
                subscribers.forEach(id -> {
                    SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(id, text, true);
                    messageSenderService.offerBotApiMethodToQueue(sendMessage);
                });
            }
        }
        log.info("UpdaterService update task done, add {} strip to db", count);
    }

    private void updateQuotes() {
        ResponseEntity<QuoteRSS> responseEntity = restTemplate.getForEntity(rssQuoteLink, QuoteRSS.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) return;

        QuoteRSS rss = responseEntity.getBody();
        List<QuoteItem> items = rss.getChannel().getItemList();
        Set<Long> subscribers = stateService.getUserIdSet();
        int maxItem = stateService.getMaxQuoteNumber();

        for (QuoteItem item : items) {
            if (item.getNumber() > stateService.getMaxQuoteNumber()) {
                if (item.getNumber() > maxItem) maxItem = item.getNumber();
                String text = item.getTextMessage();
                subscribers.forEach(id -> {
                    SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(id, text);
                    messageSenderService.offerBotApiMethodToQueue(sendMessage);
                });
            }
        }
        stateService.updateMaxQuoteNumber(maxItem);
        log.info("UpdaterService update task done, max quote number = {}", maxItem);
    }
}
