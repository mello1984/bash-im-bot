package ru.butakov.bash_im_bot.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "state")
@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class State {
    @Id
    int id;

    @Column(name = "max_quote")
    int maxQuoteNumber;
    @Column(name = "max_strip")
    int maxStripNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "strip_ids", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "strip_id")
    Set<Integer> stripSet = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_ids", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "chat_id")
    Set<Long> userSet = new HashSet<>();

    public State(int id, int maxQuote, int maxStrip) {
        this.id = id;
        this.maxQuoteNumber = maxQuote;
        this.maxStripNumber = maxStrip;
    }
}
