package ru.butakov.bash_im_bot.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuoteServiceImplTest {
    @MockBean
    RestTemplate restTemplate;
    @MockBean
    StateService stateService;
    @Autowired
    QuoteServiceImpl quoteService;
    @MockBean
    UpdaterService updaterService;

    @Test
    void getRandomQuoteSuccessfulTest() {
        String expected = "<a href=\"https://bash.im/quote/0\">#0</a>\n01.01.2000 в 9:00\ntext";

        String body = "<title>Цитата #0 – Цитатник Рунета</title><div class=\"quote__header_date\">01.01.2000 в 9:00</div><div class=\"quote__body\">text</div>";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        Mockito.doReturn(responseEntity).when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.when(stateService.getMaxQuoteNumber()).thenReturn(1);

        String actual = quoteService.getRandomQuote();

        assertEquals(expected, actual);
        Mockito.verify(restTemplate, Mockito.times(1)).getForEntity(Mockito.anyString(), Mockito.any());
    }

    @Test
    void getRandomQuoteWrongTitleTest() {
        String expected = "<a href=\"https://bash.im/quote/0\">#0</a>\n01.01.2000 в 9:00\ntext";

        String bodyMain = "<title>Цитатник Рунета</title><div class=\"quote__header_date\">01.01.2000 в 9:00</div><div class=\"quote__body\">text</div>";
        ResponseEntity<String> responseEntity1 = new ResponseEntity<>(bodyMain, HttpStatus.OK);

        String body2 = "<title>Цитата #0 – Цитатник Рунета</title><div class=\"quote__header_date\">01.01.2000 в 9:00</div><div class=\"quote__body\">text</div>";
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(body2, HttpStatus.OK);

        Mockito
                .doReturn(responseEntity1)
                .doReturn(responseEntity2)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.when(stateService.getMaxQuoteNumber()).thenReturn(1);

        String actual = quoteService.getRandomQuote();

        assertEquals(expected, actual);
        Mockito.verify(restTemplate, Mockito.times(2)).getForEntity(Mockito.anyString(), Mockito.any());
    }

    @Test
    void getRandomQuoteEmptyBodyTest() {
        String expected = "<a href=\"https://bash.im/quote/0\">#0</a>\n01.01.2000 в 9:00\ntext";

        String bodyMain = "";
        ResponseEntity<String> responseEntity1 = new ResponseEntity<>(bodyMain, HttpStatus.OK);

        String body2 = "<title>Цитата #0 – Цитатник Рунета</title><div class=\"quote__header_date\">01.01.2000 в 9:00</div><div class=\"quote__body\">text</div>";
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(body2, HttpStatus.OK);

        Mockito
                .doReturn(responseEntity1)
                .doReturn(responseEntity2)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.when(stateService.getMaxQuoteNumber()).thenReturn(1);

        String actual = quoteService.getRandomQuote();

        assertEquals(expected, actual);
        Mockito.verify(restTemplate, Mockito.times(2)).getForEntity(Mockito.anyString(), Mockito.any());

    }

    @Test
    void getRandomQuoteNullPointerTest() {
        String expected = "<a href=\"https://bash.im/quote/0\">#0</a>\n01.01.2000 в 9:00\ntext";

        ResponseEntity<String> responseEntity1 = new ResponseEntity<>(null, HttpStatus.OK);

        String body2 = "<title>Цитата #0 – Цитатник Рунета</title><div class=\"quote__header_date\">01.01.2000 в 9:00</div><div class=\"quote__body\">text</div>";
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(body2, HttpStatus.OK);

        Mockito
                .doReturn(responseEntity1)
                .doReturn(responseEntity2)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.when(stateService.getMaxQuoteNumber()).thenReturn(1);

        String actual = quoteService.getRandomQuote();

        assertEquals(expected, actual);
        Mockito.verify(restTemplate, Mockito.times(2)).getForEntity(Mockito.anyString(), Mockito.any());

    }
}