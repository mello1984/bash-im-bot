package ru.butakov.bash_im_bot.entity.rss.quote;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
@Getter
public class QuoteRSS {
    @XmlElement(name = "channel")
    QuoteChannel channel;
}
