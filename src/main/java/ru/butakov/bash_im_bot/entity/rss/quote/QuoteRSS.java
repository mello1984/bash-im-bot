package ru.butakov.bash_im_bot.entity.rss.quote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuoteRSS {
    @XmlElement(name = "channel")
    QuoteChannel channel;
}
