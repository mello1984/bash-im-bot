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

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class StateService {
    final StateRepository stateRepository;
    final State state;
    final StripRepository stripRepository;

    public StateService(@Autowired StateRepository stateRepository,
                        @Autowired StripRepository stripRepository) {
        this.stateRepository = stateRepository;
        this.stripRepository = stripRepository;
        state = loadState();
    }

    public int updateMaxQuoteNumber(int number) {
        if (state.getMaxQuoteNumber() < number) {
            state.setMaxQuoteNumber(number);
            stateRepository.save(state);
        }
        return state.getMaxQuoteNumber();
    }

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

    public void addUser(long chatId) {
        if (state.getUserSet().add(chatId)) stateRepository.save(state);
    }

    public void removeUser(long chatId) {
        if (state.getUserSet().remove(chatId)) stateRepository.save(state);
    }

    public Set<Long> getUserSet() {
        return Collections.unmodifiableSet(state.getUserSet());
    }

    public Set<StripItem> getStripItemSet() {
        return Collections.unmodifiableSet(state.getStripItems());
    }

    @Transactional
    public boolean addStripItemToDb(StripItem item) {
        boolean result = false;
        item.prepareAfterCreatingFromXml();
        if (item.isPrepared() && !state.getStripItems().contains(item)) {
            item = stripRepository.save(item);
            result = state.getStripItems().add(item);
            stateRepository.save(state);
        }
        return result;
    }
}