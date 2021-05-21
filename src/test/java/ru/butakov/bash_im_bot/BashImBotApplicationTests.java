package ru.butakov.bash_im_bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.butakov.bash_im_bot.bot.SendMessageFormat;
import ru.butakov.bash_im_bot.bot.TelegramFacade;
import ru.butakov.bash_im_bot.bot.handlers.InputMessageHandler;
import ru.butakov.bash_im_bot.dao.StateRepository;
import ru.butakov.bash_im_bot.dao.StripRepository;
import ru.butakov.bash_im_bot.service.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BashImBotApplicationTests {
    private final List<InputMessageHandler> inputMessageHandlerList;
    private final SendMessageFormat sendMessageFormat;
    private final TelegramFacade telegramFacade;
    private final StateRepository stateRepository;
    private final StripRepository stripRepository;
    private final MessageSenderService messageSenderService;
    private final QuoteService quoteService;
    private final StateService stateService;
    private final StripService stripService;
    private final UpdaterService updaterService;

    @Autowired
    public BashImBotApplicationTests(List<InputMessageHandler> inputMessageHandlerList,
                                     SendMessageFormat sendMessageFormat,
                                     TelegramFacade telegramFacade,
                                     StateRepository stateRepository,
                                     StripRepository stripRepository,
                                     MessageSenderService messageSenderService,
                                     QuoteService quoteService,
                                     StateService stateService,
                                     StripService stripService,
                                     UpdaterService updaterService) {
        this.inputMessageHandlerList = inputMessageHandlerList;
        this.sendMessageFormat = sendMessageFormat;
        this.telegramFacade = telegramFacade;
        this.stateRepository = stateRepository;
        this.stripRepository = stripRepository;
        this.messageSenderService = messageSenderService;
        this.quoteService = quoteService;
        this.stateService = stateService;
        this.stripService = stripService;
        this.updaterService = updaterService;
    }

    @Test
    void contextLoadsInputMessageHandlerList() {
        assertAll(
                () -> assertFalse(inputMessageHandlerList.isEmpty()),
                () -> assertEquals(7, inputMessageHandlerList.size())
        );
    }

    @Test
    void contextLoadsSendMessageFormatCreated() {
        assertThat(sendMessageFormat).isNotNull();
    }

    @Test
    void contextLoadsTelegramFacadeCreated() {
        assertThat(telegramFacade).isNotNull();
    }

    @Test
    void contextLoadsStateRepositoryCreated() {
        assertThat(stateRepository).isNotNull();
    }

    @Test
    void contextLoadsStripRepositoryCreated() {
        assertThat(stripRepository).isNotNull();
    }

    @Test
    void contextLoadsMessageSenderServiceCreated() {
        assertThat(messageSenderService).isNotNull();
    }

    @Test
    void contextLoadsQuoteServiceCreated() {
        assertThat(quoteService).isNotNull();
    }

    @Test
    void contextLoadsStateServiceCreated() {
        assertThat(stateService).isNotNull();
    }

    @Test
    void contextLoadsStripServiceCreated() {
        assertThat(stripService).isNotNull();
    }

    @Test
    void contextLoadsUpdaterServiceCreated() {
        assertThat(updaterService).isNotNull();
    }

}
