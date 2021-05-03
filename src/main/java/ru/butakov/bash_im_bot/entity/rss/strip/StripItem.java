package ru.butakov.bash_im_bot.entity.rss.strip;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@XmlType(name = "item")
@XmlAccessorType(XmlAccessType.NONE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "number")
public class StripItem {
    @XmlElement(name = "link")
    String link;

    boolean prepared = false;
    String textMessage;
    int number;

    public StripItem(int number) {
        this.number = number;
        link = "https://bash.im/comics/" + number;
        prepared = true;
        updateItem();
    }

    public String getTextMessage() {
        prepareItemFromXmlIfNeed();
        return textMessage;
    }

    public int getNumber() {
        prepareItemFromXmlIfNeed();
        return number;
    }


    private void prepareItemFromXmlIfNeed() {
        if (!prepared) {
            String[] s = link.split("/");
            number = Integer.parseInt(s[s.length - 1]);
            updateItem();
            prepared = true;
        }
    }

    private void updateItem() {
        String url = MessageFormat.format("<a href=\"{0}\">{1}</a>", link, "#" + number);
        textMessage = MessageFormat.format("{0}\n{1}", "#" + number, url);
    }
}
