package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.butakov.bash_im_bot.entity.rss.quote.QuoteItem;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuoteServiceImpl implements QuoteService {
    public static String QUOTE_LINK;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StateService stateService;

    String quoteLink;
    @Value("${bash.im.main.page.title}")
    String bashImMainPageTitle;

    @Override
    @Value("${bash.im.quote.link}")
    public void setQuoteLink(String quoteLink) {
        this.quoteLink = quoteLink;
        QUOTE_LINK = quoteLink;
    }

    final Random random = new Random();

    @Override
    public String getRandomQuote() {
        Optional<QuoteItem> optionalItem = Optional.empty();
        while (optionalItem.isEmpty()) {
            int rnd = random.nextInt(stateService.getMaxQuoteNumber());
            optionalItem = getQuoteItem(rnd);
        }
        return optionalItem.get().getTextMessage();
    }

    private Optional<QuoteItem> getQuoteItem(int number) {
        String url = MessageFormat.format(quoteLink, String.valueOf(number));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        });

        if (responseEntity.getBody().contains(bashImMainPageTitle)) return Optional.empty();

        String singleStringResponse = responseEntity.getBody().replaceAll("[\\n\\r\\t]", "");
        String publicationDate = null, publicationBody = null;

        Pattern patternDate = Pattern.compile("<div class=\"quote__header_date\">(.*?)</div>");
        Matcher matcher = patternDate.matcher(singleStringResponse);
        if (matcher.find()) publicationDate = matcher.group(1).trim();

        Pattern patternBody = Pattern.compile("<div class=\"quote__body\">(.*?)</?div.*>");
        matcher = patternBody.matcher(singleStringResponse);
        if (matcher.find()) publicationBody = matcher.group(1).trim();

        QuoteItem item = new QuoteItem(number, publicationDate, publicationBody);
        return Optional.of(item);
    }

}
