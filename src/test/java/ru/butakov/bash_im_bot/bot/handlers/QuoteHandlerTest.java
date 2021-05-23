package ru.butakov.bash_im_bot.bot.handlers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.butakov.bash_im_bot.AbstractTest;
import ru.butakov.bash_im_bot.bot.SendMessageFormat;
import ru.butakov.bash_im_bot.service.QuoteService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuoteHandlerTest extends AbstractTest {
    @MockBean
    QuoteService quoteService;
    @MockBean
    SendMessageFormat sendMessageFormat;
    @Value("${bot.button.quote}")
    String command;
    @Autowired
    QuoteHandler quoteHandler;

    @Test
    void getAnswerTextTest() {
        String expectedText = "quote text";
        Mockito.when(quoteService.getRandomQuote()).thenReturn(expectedText);
        String actualText = quoteHandler.getAnswerText();

        Mockito.verify(quoteService, Mockito.times(1)).getRandomQuote();
        assertEquals(expectedText, actualText);
    }

    @Test
    void getCommandStringTest() {
        assertEquals(command, quoteHandler.getCommandString());
    }
}