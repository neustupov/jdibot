package org.neustupov.javadevinterviewbot.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReplyMessageServiceTest {

  @Autowired
  private ReplyMessageService replyMessageService;

  @Test
  void getReplyMessage() {
    assertEquals(replyMessageService.getReplyMessage(100L, "reply.search").getText(),
        "Введите фразу для поиска.");
  }

  @Test
  void getReplyText() {
    assertEquals(replyMessageService.getReplyText("reply.search"), "Введите фразу для поиска.");
  }

  @Test
  void getReplyTextArgs() {
    assertEquals(replyMessageService.getReplyText("reply.search", 1), "Введите фразу для поиска.");
  }

  @Test
  void getReplyMessageArgs() {
    assertEquals(replyMessageService.getReplyMessage(100L, "reply.search", 1).getText(),
        "Введите фразу для поиска.");
  }
}