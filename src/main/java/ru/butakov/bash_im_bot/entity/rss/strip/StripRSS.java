package ru.butakov.bash_im_bot.entity.rss.strip;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
@Getter
public class StripRSS {
    @XmlElement(name = "channel")
    StripChannel channel;
}
