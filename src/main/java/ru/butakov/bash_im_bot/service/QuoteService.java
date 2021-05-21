package ru.butakov.bash_im_bot.service;

import org.springframework.beans.factory.annotation.Value;

public interface QuoteService {
    @Value("${bash.im.quote.link}")
    void setQuoteLink(String quoteLink);

    String getRandomQuote();
}
