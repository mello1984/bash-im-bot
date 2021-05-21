package ru.butakov.bash_im_bot.entity.rss.quote;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.butakov.bash_im_bot.service.QuoteServiceImpl;

import javax.xml.bind.annotation.*;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@XmlType(name = "item")
@XmlAccessorType(XmlAccessType.NONE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class QuoteItem {
    @XmlElement(name = "link")
    String link;
    @XmlElement(name = "title")
    String title;
    @XmlElement(name = "pubDate")
    String pubDate;
    @XmlElement(name = "description")
    String description;

    boolean prepared = false;
    String textMessage;
    int number;

    public QuoteItem(int number, String pubDate, String description) {
        this.number = number;
        this.pubDate = pubDate;
        this.description = description;

        link = MessageFormat.format(QuoteServiceImpl.QUOTE_LINK, String.valueOf(number));
        updateItem();
    }

    private void prepareItemFromXmlIfNeed() {
        if (!prepared) {
            title = title.substring(title.indexOf("#"));
            number = Integer.parseInt(title.substring(1));

            DateTimeFormatter dtfFrom = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            LocalDateTime ldt = LocalDateTime.parse(pubDate, dtfFrom);
            DateTimeFormatter dtfTo = DateTimeFormatter.ofPattern("dd.MM.yyyy в HH:mm");
            pubDate = dtfTo.format(ldt);
            updateItem();
        }
    }

    private void updateItem() {
        description = description.replaceAll("(<br>|<br />)", "\n");
        String url = MessageFormat.format("<a href=\"{0}\">#{1}</a>", link, String.valueOf(number));
        textMessage = MessageFormat.format("{0}\n{1}\n{2}", url, pubDate, description);
        prepared = true;
    }

    public String getTextMessage() {
        prepareItemFromXmlIfNeed();
        return textMessage;
    }

    public int getNumber() {
        prepareItemFromXmlIfNeed();
        return number;
    }
}
