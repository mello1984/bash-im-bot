package ru.butakov.bash_im_bot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.butakov.bash_im_bot.AbstractTest;
import ru.butakov.bash_im_bot.dao.MaxQuoteNumberRepository;
import ru.butakov.bash_im_bot.dao.StripRepository;
import ru.butakov.bash_im_bot.dao.UserRepository;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;
import ru.butakov.bash_im_bot.entity.state.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Sql(value = "/create-db-test-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/create-db-test-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StateServiceImplTest extends AbstractTest {
    @Autowired
    StateService stateService;
    @Autowired
    MaxQuoteNumberRepository maxQuoteNumberRepository;
    @Autowired
    StripRepository stripRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void updateMaxQuoteNumberFailedTest() {
        int actual = stateService.updateMaxQuoteNumber(10);
        assertEquals(100, actual);
        assertEquals(100, maxQuoteNumberRepository.findById(1).get().getMaxQuoteNumber());
    }

    @Test
    void updateMaxQuoteNumberSuccessfulTest() {
        int expected = stateService.updateMaxQuoteNumber(200);
        assertEquals(200, expected);
        assertEquals(200, maxQuoteNumberRepository.findById(1).get().getMaxQuoteNumber());
    }

    @Test
    void getMaxQuoteNumberTest() {
        assertEquals(100, stateService.getMaxQuoteNumber());
    }

    @Test
    void addNewUserTest() {
        stateService.addUser(300);
        assertEquals(3, userRepository.count());

        Set<User> expected = Set.of(new User(100), new User(200), new User(300));
        Set<User> actual = new HashSet<>(userRepository.findAll());
        assertEquals(expected, actual);
    }

    @Test
    void addExistingUserTest() {
        stateService.addUser(100);
        assertEquals(2, userRepository.count());

        Set<User> expected = Set.of(new User(100), new User(200));
        Set<User> actual = new HashSet<>(userRepository.findAll());
        assertEquals(expected, actual);
    }

    @Test
    void removeExistingUserTest() {
        stateService.removeUser(100);
        assertEquals(1, userRepository.count());
        assertEquals(Collections.singletonList(new User(200)), userRepository.findAll());
    }

    @Test
    void removeNotExistingUserTest() {
        stateService.removeUser(400);
        assertEquals(2, userRepository.count());

        Set<User> expected = Set.of(new User(100), new User(200));
        Set<User> actual = new HashSet<>(userRepository.findAll());
        assertEquals(expected, actual);
    }

    @Test
    void getUserIdSetTest() {
        Set<Long> expected = Set.of(100L, 200L);
        Set<Long> actual = stateService.getUserIdSet();
        assertEquals(expected, actual);

    }

    @Test
    void getUserCountTest() {
        assertEquals(2, stateService.getUserCount());
    }

    @Test
    void getStripItemSetTest() {
        Set<StripItem> expected = Set.of(new StripItem("image1.jpg", 20110101),
                new StripItem("image2.jpg", 20120202),
                new StripItem("image3.jpg", 20130303),
                new StripItem("image4.jpg", 20140404),
                new StripItem("image5.jpg", 20150505));
        Set<StripItem> actual = stateService.getStripItemSet();
        assertEquals(5, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void getStripCountTest() {
        assertEquals(5, stateService.getStripCount());
    }

    @Test
    void addStripItemToDbAcceptedTest() {
        StripItem stripItem = new StripItem("image10.jpg", 20200101);
        boolean actual = stateService.addStripItemToDb(stripItem);
        assertTrue(actual);
        assertEquals(6, stripRepository.count());
        assertTrue(stateService.getStripItemSet().contains(stripItem));

    }

    @Test
    void addStripItemToDbDeclinedTest() {
        StripItem stripItem = new StripItem("image1.jpg", 20110101);
        boolean actual = stateService.addStripItemToDb(stripItem);
        assertFalse(actual);
        assertEquals(5, stripRepository.count());
        assertTrue(stateService.getStripItemSet().contains(stripItem));
    }

    @Test
    void clearStripItems() {
        stateService.clearStripItems();
        assertEquals(0, stripRepository.count());
    }
}