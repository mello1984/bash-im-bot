package ru.butakov.bash_im_bot.entity.rss.strip;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlType
@XmlAccessorType(XmlAccessType.NONE)
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StripChannel {
    @XmlElement(name = "item")
    List<StripItem> itemList = new ArrayList<>();
}
