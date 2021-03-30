package org.neustupov.javadevinterviewbot.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.utils.Emojis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocaleMessageServiceTest {

  @Autowired
  private LocaleMessageService localeMessageService;

  @Test
  void getMessageOneArg() {
    String message = localeMessageService.getMessage("reply.menu");
    assertEquals(message, "С чего начнем?");
  }

  @Test
  void getMessageMoreArgs() {
    String message = localeMessageService.getMessage("reply.pictureText", Emojis.WAVE);
    assertEquals(message, "\uD83D\uDC4B Привет инженер! \uD83D\uDC4B");
  }
}