package ru.butakov.bash_im_bot.entity.state;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "max_quote_number")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaxQuoteNumber {
    @Id
    int id;
    @Column(name = "max_quote_number")
    int maxQuoteNumber;
}
