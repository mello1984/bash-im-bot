package ru.butakov.bash_im_bot.entity.rss.strip;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.text.MessageFormat;

@XmlType(name = "item")
@XmlAccessorType(XmlAccessType.NONE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, doNotUseGetters = true)
@Entity
@Table(name = "strip")
public class StripItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    int id;

    @XmlElement(name = "description")
    @Transient
    String description;
    @Column
    @XmlElement(name = "link")
    String link;

    @Column
    @EqualsAndHashCode.Include
    int number;
    @Transient
    boolean prepared = false;
    @Transient
    String textMessage;


//    public StripItem(int number) {
//        this.number = number;
//        link = "https://bash.im/comics/" + number;
//        prepared = true;
//        updateItem();
//    }

    public String getTextMessage() {
//        if (!prepared) prepareItemFromXmlIfNeed();
        String url = MessageFormat.format("<a href=\"{0}\">{1}</a>", link, "#" + number);
//        textMessage = MessageFormat.format("{0}\n{1}", "#" + number, url);
        return url;
    }

    public int getNumber() {
//        if (!prepared) prepareItemFromXmlIfNeed();
        return number;
    }


    public StripItem prepareItemFromXmlIfNeed() {
        if (!prepared) {
            number = Integer.parseInt(link.replaceAll("https://bash.im/comics/", ""));
            link = description.replaceAll("<img src=\"", "").replaceAll("\">", "");

//            updateItem();
            prepared = true;
        }
        return this;
    }

    private void updateItem() {
        String url = MessageFormat.format("<a href=\"{0}\">{1}</a>", link, "#" + number);
        textMessage = MessageFormat.format("{0}\n{1}", "#" + number, url);
    }
}
