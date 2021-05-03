package ru.butakov.bash_im_bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.butakov.bash_im_bot.dao.StateRepository;
import ru.butakov.bash_im_bot.entity.State;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateService {
    final StateRepository stateRepository;
    final State state;

    public StateService(@Autowired StateRepository stateRepository) {
        this.stateRepository = stateRepository;
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
            resultState = new State(1, 0, 0);
            stateRepository.save(resultState);
        }
        return stateOptional.orElse(resultState);
    }

    public void addUser(long chatId) {
        if (state.getUserSet().add(chatId)) save(state);
    }

    public void removeUser(long chatId) {
        if (state.getUserSet().remove(chatId)) save(state);
    }

    public Set<Long> getSubscribers() {
        return Collections.unmodifiableSet(state.getUserSet());
    }

    public Set<Integer> getStripsSet() {
        return Collections.unmodifiableSet(state.getStripSet());
    }

    public boolean addStripToDb(int number) {
        boolean result = state.getStripSet().add(number);
        if (result) {
            save(state);
        }
        return result;
    }

    /**
     * It's really strange, but by default hibernate duplicate entity in one-to-many relations, and it work in Java because
     * Set removes duplicates. I don't know solution, so create this method. It's bad, but work...
     */
    private void save(State state) {
        Set<Integer> strips = state.getStripSet();
        state.setStripSet(Collections.emptySet());
        Set<Long> users = state.getUserSet();
        state.setStripSet(Collections.emptySet());
        stateRepository.save(state);

        state.setStripSet(strips);
        state.setUserSet(users);
        stateRepository.save(state);
    }
}
