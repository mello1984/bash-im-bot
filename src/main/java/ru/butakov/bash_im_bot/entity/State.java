package ru.butakov.bash_im_bot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "state")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class State {
    @Id
    @Column
    int id;
    @Column(name = "max_quote")
    int maxQuoteNumber;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "strip_id")
    Set<StripItem> stripItems = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_ids", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "chat_id")
    Set<Long> userSet = ConcurrentHashMap.newKeySet();

    public State(int id, int maxQuote) {
        this.id = id;
        this.maxQuoteNumber = maxQuote;
    }
}
