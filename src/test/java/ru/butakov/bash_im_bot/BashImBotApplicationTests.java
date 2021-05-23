package ru.butakov.bash_im_bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.butakov.bash_im_bot.bot.SendMessageFormat;
import ru.butakov.bash_im_bot.bot.TelegramFacade;
import ru.butakov.bash_im_bot.bot.handlers.InputMessageHandler;
import ru.butakov.bash_im_bot.dao.MaxQuoteNumberRepository;
import ru.butakov.bash_im_bot.dao.StripRepository;
import ru.butakov.bash_im_bot.dao.UserRepository;
import ru.butakov.bash_im_bot.service.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BashImBotApplicationTests {
    List<InputMessageHandler> inputMessageHandlerList;
    SendMessageFormat sendMessageFormat;
    TelegramFacade telegramFacade;
    MaxQuoteNumberRepository maxQuoteNumberRepository;
    StripRepository stripRepository;
    UserRepository userRepository;
    MessageSenderService messageSenderService;
    QuoteService quoteService;
    StateService stateService;
    StripService stripService;
    UpdaterService updaterService;

    @Autowired
    public BashImBotApplicationTests(List<InputMessageHandler> inputMessageHandlerList, SendMessageFormat sendMessageFormat, TelegramFacade telegramFacade, MaxQuoteNumberRepository maxQuoteNumberRepository, StripRepository stripRepository, UserRepository userRepository, MessageSenderService messageSenderService, QuoteService quoteService, StateService stateService, StripService stripService, UpdaterService updaterService) {
        this.inputMessageHandlerList = inputMessageHandlerList;
        this.sendMessageFormat = sendMessageFormat;
        this.telegramFacade = telegramFacade;
        this.maxQuoteNumberRepository = maxQuoteNumberRepository;
        this.stripRepository = stripRepository;
        this.userRepository = userRepository;
        this.messageSenderService = messageSenderService;
        this.quoteService = quoteService;
        this.stateService = stateService;
        this.stripService = stripService;
        this.updaterService = updaterService;
    }

    @Test
    void contextLoadsInputMessageHandlerListTest() {
        assertAll(
                () -> assertFalse(inputMessageHandlerList.isEmpty()),
                () -> assertEquals(7, inputMessageHandlerList.size())
        );
    }

    @Test
    void contextLoadsSendMessageFormatCreatedTest() {
        assertThat(sendMessageFormat).isNotNull();
    }

    @Test
    void contextLoadsTelegramFacadeCreatedTest() {
        assertThat(telegramFacade).isNotNull();
    }

    @Test
    void contextLoadsMaxQuoteNumberRepositoryTest() {
        assertThat(maxQuoteNumberRepository).isNotNull();
    }

    @Test
    void contextLoadsStripRepositoryTest() {
        assertThat(stripRepository).isNotNull();
    }

    @Test
    void contextLoadsUserRepositoryCreatedTest() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    void contextLoadsStripRepositoryCreatedTest() {
        assertThat(stripRepository).isNotNull();
    }

    @Test
    void contextLoadsMessageSenderServiceCreatedTest() {
        assertThat(messageSenderService).isNotNull();
    }

    @Test
    void contextLoadsQuoteServiceCreatedTest() {
        assertThat(quoteService).isNotNull();
    }

    @Test
    void contextLoadsStateServiceCreatedTest() {
        assertThat(stateService).isNotNull();
    }

    @Test
    void contextLoadsStripServiceCreatedTest() {
        assertThat(stripService).isNotNull();
    }

    @Test
    void contextLoadsUpdaterServiceCreatedTest() {
        assertThat(updaterService).isNotNull();
    }

}
