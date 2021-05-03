package ru.butakov.bash_im_bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

    @Scheduled(fixedDelayString = "${bash.im.update.delay}")
    private void updataData() {
        updateQuotes();
        updateStrips();
    }

    private void updateStrips() {
        ResponseEntity<StripRSS> responseEntity = restTemplate.exchange(
                rssStripLink,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) return;

        StripRSS rss = responseEntity.getBody();
        List<StripItem> items = rss.getChannel().getItemList();
        Set<Long> subscribers = stateService.getSubscribers();

        for (StripItem item : items) {
            if (stateService.addStripToDb(item.getNumber())) {
                String text = item.getTextMessage();
                subscribers.forEach(id -> {
                    SendMessage sendMessage = sendMessageFormat.getSendMessageBaseFormat(id, text);
                    messageSenderService.offerBotApiMethodToQueue(sendMessage);
                });
                log.info("UpdaterService update task done, add strip to db: {}", item.getNumber());
            }
        }
    }


    private void updateQuotes() {
        ResponseEntity<QuoteRSS> responseEntity = restTemplate.exchange(
                rssQuoteLink,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) return;

        QuoteRSS rss = responseEntity.getBody();
        List<QuoteItem> items = rss.getChannel().getItemList();
        int maxItem = stateService.getMaxQuoteNumber();
        Set<Long> subscribers = stateService.getSubscribers();
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
