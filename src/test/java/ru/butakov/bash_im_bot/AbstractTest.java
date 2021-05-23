package ru.butakov.bash_im_bot;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.butakov.bash_im_bot.bot.BashBot;
import ru.butakov.bash_im_bot.service.UpdaterService;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public abstract class AbstractTest {
    @MockBean
    protected UpdaterService updaterService;
    @MockBean
    protected BashBot bashBot;
}
