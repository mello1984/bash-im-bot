package ru.butakov.bash_im_bot.bot.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.butakov.bash_im_bot.service.QuoteService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuoteHandler extends AbstractHandler {
    @Autowired
    QuoteService quoteService;
    @Value("${bot.button.quote}")
    String command;

    @Override
    protected String getAnswerText() {
       return quoteService.getRandomQuote();
    }

    @Override
    public String getCommandString() {
        return command;
    }
}
