package ru.butakov.bash_im_bot.entity.rss.strip;

import lombok.*;
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
@Getter
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
    @Getter
    boolean prepared;

    public StripItem(String link, int number) {
        this.link = link;
        this.number = number;
        prepared = true;
    }

    public String getTextMessage() {
        return MessageFormat.format("<a href=\"https://bash.im/img/{0}\">#{1}</a>", link, String.valueOf(number));
    }

    public void prepareAfterCreatingFromXml() {
        if (!prepared) {
            number = Integer.parseInt(link.replaceAll("https://bash.im/comics/", ""));
            link = description.replaceAll("(<img src=\"|\">)", "")
                    .replaceAll("https://bash.im/img/", "");
            prepared = true;
        }
    }
}
