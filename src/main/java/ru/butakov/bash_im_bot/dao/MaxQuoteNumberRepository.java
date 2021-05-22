package ru.butakov.bash_im_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.bash_im_bot.entity.state.MaxQuoteNumber;

public interface MaxQuoteNumberRepository extends JpaRepository<MaxQuoteNumber, Integer> {
}
