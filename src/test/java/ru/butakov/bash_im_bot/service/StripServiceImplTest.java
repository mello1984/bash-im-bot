package ru.butakov.bash_im_bot.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.butakov.bash_im_bot.AbstractTest;
import ru.butakov.bash_im_bot.entity.rss.strip.StripItem;

import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

//@Sql(value = "/create-db-test-after.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = "/create-db-test-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class StripServiceImplTest extends AbstractTest {
    @MockBean
    StateService stateService;
    @MockBean
    RestTemplate restTemplate;
    @Value("${bash.im.strip.monthlink}")
    String stripLink;
    @Autowired
    StripServiceImpl stripService;

    @Test
    void getRandomStrip() {
        Mockito.when(stateService.getStripItemSet()).thenReturn(Set.of(new StripItem("link1", 1)));

        String expected = "<a href=\"https://bash.im/img/link1\">#1</a>";
        String actual = stripService.getRandomStrip();
        assertEquals(expected, actual);
    }

    @Test
    void generateStripDbSuccessfulTest() {
        String body = "anotherText<img class=\"quote__img\" data-src=\"/img/image1.png\" alt=\"\"/>anothertextСтрип <a href=\"/strip/20200101\">anotherText";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        Mockito.doReturn(responseEntity)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        stripService.generateStripDb(202001, 202002);

        Mockito.verify(restTemplate, Mockito.times(2)).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.verify(stateService, Mockito.times(2)).addStripItemToDb(Mockito.any());
    }

    @Test
    void generateStripDbSuccessfulTest2() {
        String body = "anotherText<img class=\"quote__img\" data-src=\"/img/image1.png\" alt=\"\"/>anothertextСтрип <a href=\"/strip/20200101\">anotherText";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        Mockito.doReturn(responseEntity)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        stripService.generateStripDb(202001, 202004);

        Mockito.verify(restTemplate, Mockito.times(4)).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.verify(stateService, Mockito.times(4)).addStripItemToDb(Mockito.any());
    }

    @Test
    void generateStripDbExceptionTest() {
        String body = "anotherText<img class=\"quote__img\" data-src=\"/img/image1.png\" alt=\"\"/>anothertextСтрип <a href=\"/strip/20200101\">anotherText";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        Mockito
                .doReturn(responseEntity)
                .doThrow(new RestClientException("test"))
                .doReturn(responseEntity)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        stripService.generateStripDb(202001, 202003);

        Mockito.verify(restTemplate, Mockito.times(3)).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.verify(stateService, Mockito.times(2)).addStripItemToDb(Mockito.any());
    }

    @Test
    void generateStripDbWrongAnswerTest() {
        String body = "anotherText<img class=\"quote__img\" data-src=\"/img/image1.png\" alt=\"\"/>anothertextСтрип <a href=\"/strip/20200101\">anotherText";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);

        String bodyWrong = "anotherText";
        ResponseEntity<String> responseEntity2 = new ResponseEntity<>(bodyWrong, HttpStatus.OK);

        Mockito
                .doReturn(responseEntity)
                .doReturn(responseEntity2)
                .doReturn(responseEntity)
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.any());
        stripService.generateStripDb(202001, 202003);

        Mockito.verify(restTemplate, Mockito.times(3)).getForEntity(Mockito.anyString(), Mockito.any());
        Mockito.verify(stateService, Mockito.times(2)).addStripItemToDb(Mockito.any());
    }
}