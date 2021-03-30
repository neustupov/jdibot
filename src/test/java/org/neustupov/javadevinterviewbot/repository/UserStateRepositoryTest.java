package org.neustupov.javadevinterviewbot.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.botapi.states.BotState;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

@DataMongoTest
@ActiveProfiles("test")
class UserStateRepositoryTest {

  @Autowired
  private UserStateRepository userStateRepository;

  private UserState userStateOne;
  private UserState userStateTwo;

  @BeforeEach
  void setUp() {
    userStateOne = GenericBuilder.of(UserState::new)
        .with(UserState::setUserId, 250L)
        .with(UserState::setBotState, BotState.SHOW_LEVEL_MENU)
        .build();
    userStateTwo = GenericBuilder.of(UserState::new)
        .with(UserState::setUserId, 350L)
        .with(UserState::setBotState, BotState.SHOW_CATEGORY)
        .build();
  }

  @Test
  void saveTest(){
    userStateRepository.save(userStateOne);

    assertTrue(userStateRepository.findById(250L).isPresent());
    assertEquals(userStateRepository.findById(250L).get().getBotState(), BotState.SHOW_LEVEL_MENU);
  }

  @Test
  void findAllTest() {
    userStateRepository.save(userStateOne);
    userStateRepository.save(userStateTwo);

    assertFalse(userStateRepository.findAll().isEmpty());
    assertEquals(userStateRepository.findAll().size(), 2);
  }
}