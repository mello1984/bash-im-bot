package ru.butakov.bash_im_bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.butakov.bash_im_bot.dao.MaxQuoteNumberRepository;
import ru.butakov.bash_im_bot.dao.StripRepository;
import ru.butakov.bash_im_bot.dao.UserRepository;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;
import ru.butakov.bash_im_bot.entity.state.MaxQuoteNumber;
import ru.butakov.bash_im_bot.entity.state.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StateServiceImpl implements StateService {
    MaxQuoteNumberRepository maxQuoteNumberRepository;
    UserRepository userRepository;
    StripRepository stripRepository;

    @Autowired
    public StateServiceImpl(MaxQuoteNumberRepository maxQuoteNumberRepository,
                            UserRepository userRepository,
                            StripRepository stripRepository) {
        this.maxQuoteNumberRepository = maxQuoteNumberRepository;
        this.userRepository = userRepository;
        this.stripRepository = stripRepository;
    }

    @Override
    public int updateMaxQuoteNumber(int number) {
        MaxQuoteNumber maxQuoteNumber = maxQuoteNumberRepository.findById(1).orElseGet(() -> {
            MaxQuoteNumber quoteNumber = new MaxQuoteNumber(1, 1);
            maxQuoteNumberRepository.save(quoteNumber);
            return quoteNumber;
        });

        if (number > maxQuoteNumber.getMaxQuoteNumber()) {
            maxQuoteNumber.setMaxQuoteNumber(number);
            maxQuoteNumberRepository.save(maxQuoteNumber);
        }

        return number > maxQuoteNumber.getMaxQuoteNumber() ? number : maxQuoteNumber.getMaxQuoteNumber();
    }

    @Override
    public int getMaxQuoteNumber() {
        return maxQuoteNumberRepository
                .findById(1)
                .orElseGet(() -> {
                    MaxQuoteNumber maxQuoteNumber = new MaxQuoteNumber(1, 1);
                    maxQuoteNumberRepository.save(maxQuoteNumber);
                    return maxQuoteNumber;
                })
                .getMaxQuoteNumber();
    }

    @Override
    public void addUser(long chatId) {
        if (userRepository.findById(chatId).isEmpty()) userRepository.save(new User(chatId));
    }

    @Override
    public void removeUser(long chatId) {
        if (userRepository.findById(chatId).isPresent()) userRepository.deleteById(chatId);
    }

    @Override
    public Set<Long> getUserIdSet() {
        return userRepository
                .findAll()
                .stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public int getUserCount() {
        return (int) userRepository.count();
    }

    @Override
    public Set<StripItem> getStripItemSet() {
        return new HashSet<>(stripRepository.findAll());
    }

    @Override
    public int getStripCount() {
        return (int) stripRepository.count();
    }

    @Override
    public boolean addStripItemToDb(StripItem item) {
        boolean result = false;
        item.prepareAfterCreatingFromXml();

        if (stripRepository.findByNumber(item.getNumber()).isEmpty()) {
            stripRepository.save(item);
            result = true;
        }
        return result;
    }

    @Override
    public void clearStripItems() {
        stripRepository.deleteAll();
    }
}
