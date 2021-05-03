package ru.butakov.bash_im_bot.entity.rss.quote;

import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlType
@XmlAccessorType(XmlAccessType.NONE)
@Getter
public class QuoteChannel {
    @XmlElement(name = "item")
    List<QuoteItem> itemList = new ArrayList<>();
}
