package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.bash_im_bot.dao.StateRepository;
import ru.butakov.bash_im_bot.dao.StripRepository;
import ru.butakov.bash_im_bot.entity.State;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class StateServiceImpl implements StateService {
    final StateRepository stateRepository;
    final State state;
    final StripRepository stripRepository;
    final ReentrantLock lock = new ReentrantLock();

    public StateServiceImpl(@Autowired StateRepository stateRepository,
                            @Autowired StripRepository stripRepository) {
        this.stateRepository = stateRepository;
        this.stripRepository = stripRepository;
        state = loadState();
    }

    @Override
    public int updateMaxQuoteNumber(int number) {
        if (state.getMaxQuoteNumber() < number) {
            state.setMaxQuoteNumber(number);
            stateRepository.save(state);
        }
        return state.getMaxQuoteNumber();
    }

    @Override
    public int getMaxQuoteNumber() {
        return state.getMaxQuoteNumber();
    }

    private State loadState() {
        Optional<State> stateOptional = stateRepository.findById(1);
        State resultState = null;
        if (stateOptional.isEmpty()) {
            resultState = new State(1, 0);
            stateRepository.save(resultState);
        }
        return stateOptional.orElse(resultState);
    }

    @Override
    public void addUser(long chatId) {
        if (state.getUserSet().add(chatId)) stateRepository.save(state);
    }

    @Override
    public void removeUser(long chatId) {
        if (state.getUserSet().remove(chatId)) stateRepository.save(state);
    }

    @Override
    public Set<Long> getUserSet() {
        return Collections.unmodifiableSet(state.getUserSet());
    }

    @Override
    public int getUserCount() {
        return state.getUserSet().size();
    }

    @Override
    public Set<StripItem> getStripItemSet() {
        return Collections.unmodifiableSet(state.getStripItems());
    }

    @Override
    public int getStripCount() {
        return state.getStripItems().size();
    }

    @Override
    public boolean addStripItemToDb(StripItem item) {
        boolean result = false;
        item.prepareAfterCreatingFromXml();
        if (item.isPrepared() && !state.getStripItems().contains(item)) {
            boolean locked = false;
            try {
                locked = lock.tryLock();
                if (locked) {
                    item = stripRepository.save(item);
                    result = state.getStripItems().add(item);
                    stateRepository.save(state);
                }
            } finally {
                if (locked) lock.unlock();
            }
        }
        return result;
    }

    @Override
    public void clearStripItems() {
        try {
            lock.lock();
            state.setStripItems(new HashSet<>());
            stateRepository.save(state);
        } finally {
            lock.unlock();
        }
    }
}
