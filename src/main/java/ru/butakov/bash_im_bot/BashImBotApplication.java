package ru.butakov.bash_im_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "ru.butakov.bash_im_bot.*")
@EnableScheduling
public class BashImBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BashImBotApplication.class, args);
    }

}
