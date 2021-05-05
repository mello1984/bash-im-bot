package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StripService {
    StateService stateService;
    RestTemplate restTemplate;
    int firstPeriod;
    int lastPeriod;
    String stripLink;
    Random random = new Random();

    public StripService(@Autowired StateService stateService,
                        @Autowired RestTemplate restTemplate,
                        @Value("${bash.im.strips.need.updatedb}") boolean needCreateStripBase,
                        @Value("${bash.im.strips.updatedb.firstperiod}") int firstPeriod,
                        @Value("${bash.im.strips.updatedb.lastperiod}") int lastPeriod,
                        @Value("${bash.im.strip.monthlink}") String stripLink) {
        this.stateService = stateService;
        this.restTemplate = restTemplate;
        this.firstPeriod = firstPeriod;
        this.lastPeriod = lastPeriod;
        this.stripLink = stripLink;
        if (needCreateStripBase) generateStripDb();
    }

    public String getRandomStrip() {
        Set<StripItem> strips = stateService.getStripItemSet();
        int rnd = random.nextInt(strips.size() - 1);
        StripItem stripItem = strips.stream().skip(rnd).findFirst().get();
        return stripItem.getTextMessage();
    }

    public void generateStripDb() {
        generateStripDb(firstPeriod, lastPeriod);
    }

    public void generateStripDb(int firstPeriod, int lastPeriod) {
        List<Integer> periods = generatePeriodsList(firstPeriod, lastPeriod);

        Pattern patternLink = Pattern.compile("<img class=\"quote__img\" data-src=\"/img/(.*?)\" alt=\"\"/>");
        Pattern patternNumber = Pattern.compile("Стрип <a href=\"/strip/(\\d{8})\">");

        for (int period : periods) {
            ResponseEntity<String> responseEntity;
            try {
                responseEntity = restTemplate.getForEntity(stripLink + period, String.class);
            } catch (RestClientException ignored) {
                log.warn("Generate strip database warn, exception in create ResponseEntity for period {}", period);
                continue;
            }

            String singleStringResponse = responseEntity.getBody().replaceAll("[\\n\\r\\t]", "");

            List<String> links = new ArrayList<>();
            List<String> numbers = new ArrayList<>();
            Matcher matcher = patternLink.matcher(singleStringResponse);
            while (matcher.find()) {
                links.add(matcher.group(1).trim());
            }

            matcher = patternNumber.matcher(singleStringResponse);
            while (matcher.find()) {
                numbers.add(matcher.group(1).trim());
            }

            if (numbers.size() != links.size()) {
                log.warn("Generate strip database warn, numbers.size()!= links.size() on page {}", period);
                continue;
            }
            for (int i = 0; i < numbers.size(); i++) {
                StripItem item = new StripItem(links.get(i), Integer.parseInt(numbers.get(i)));
                stateService.addStripItemToDb(item);
            }
            log.info("Add {} strip to DB, for period {}", numbers.size(), period);
        }
    }


    private List<Integer> generatePeriodsList(int first, int last) {
        if (Integer.toString(first).length() != 6 || Integer.toString(last).length() != 6)
            return Collections.emptyList();

        List<Integer> list = new ArrayList<>();
        for (int i = first / 100; i <= last / 100; i++) {
            for (int j = 1; j <= 12; j++) {
                int x = i * 100 + j;
                if (x >= first && x <= last) list.add(x);
            }
        }
        return list;
    }
}
