package ru.butakov.bash_im_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.Optional;

public interface StripRepository extends JpaRepository<StripItem, Integer> {
    Optional<StripItem> findByNumber(int number);
}
