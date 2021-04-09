package org.neustupov.javadevinterviewbot.cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.model.menu.Category;
import org.neustupov.javadevinterviewbot.model.menu.Level;
import org.neustupov.javadevinterviewbot.model.GenericBuilder;
import org.neustupov.javadevinterviewbot.model.RangePair;
import org.neustupov.javadevinterviewbot.model.UserContext;
import org.neustupov.javadevinterviewbot.model.UserState;
import org.neustupov.javadevinterviewbot.repository.UserContextRepository;
import org.neustupov.javadevinterviewbot.repository.UserStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserDataCacheTest {

  @Autowired
  private UserDataCache userDataCache;

  @MockBean
  private UserContextRepository contextRepository;

  @MockBean
  private UserStateRepository userStateRepository;

  private RangePair rangePair;

  @BeforeEach
  void setUp() {
    rangePair = RangePair.builder().from(0).to(1).build();

    Optional<UserState> userState = Optional.of(GenericBuilder.of(UserState::new)
        .with(UserState::setUserId, 100L)
        .with(UserState::setBotState, BotState.SHOW_LEVEL_MENU)
        .build());
    when(userStateRepository.findById(anyLong())).thenReturn(userState);

    Optional<UserContext> userContext = Optional.of(GenericBuilder.of(UserContext::new)
        .with(UserContext::setUserId, 101L)
        .build());
    when(contextRepository.findById(anyLong())).thenReturn(userContext);
  }

  @Test
  void setUserCurrentBotState() {
    userDataCache.setUserCurrentBotState(100L, BotState.SHOW_START_MENU);
    assertEquals(userDataCache.getUserCurrentBotState(100L), BotState.SHOW_START_MENU);

    userDataCache.setUserCurrentBotState(100L, BotState.SHOW_CATEGORY);
    assertEquals(userDataCache.getUserCurrentBotState(100L), BotState.SHOW_CATEGORY);
  }

  @Test
  void getUserCurrentBotState() {
    assertEquals(userDataCache.getUserCurrentBotState(100L), BotState.SHOW_LEVEL_MENU);
  }

  @Test
  void setUserLevel() {
    userDataCache.setUserLevel(101L, Level.JUNIOR);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getLevel(), Level.JUNIOR);
  }

  @Test
  void setCategory() {
    userDataCache.setCategory(101L, Category.OOP);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getCategory(), Category.OOP);
  }

  @Test
  void setRoute() {
    userDataCache.setRoute(101L, "next");
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getRoute(), "next");
  }

  @Test
  void setSearchField() {
    userDataCache.setSearchField(101L, "abc");
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getSearchField(), "abc");
  }

  @Test
  void setRange() {
    userDataCache.setRange(101L, rangePair);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getRange().getFrom(), 0);
    assertEquals(contextRepository.findById(101L).get().getRange().getTo(), 1);
  }

  @Test
  void cleanStates() {
    userDataCache.setUserCurrentBotState(100L, BotState.SHOW_START_MENU);
    assertEquals(userDataCache.getUserCurrentBotState(100L), BotState.SHOW_START_MENU);

    userDataCache.cleanStates(101L);

    assertNull(userDataCache.getUserCurrentBotState(100L));
  }

  @Test
  void cleanSearch() {
    userDataCache.setSearchField(101L, "abc");
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getSearchField(), "abc");

    userDataCache.cleanSearch(101L);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getSearchField(), "");
  }

  @Test
  void cleanRange() {
    userDataCache.setRange(101L, rangePair);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getRange(), rangePair);

    userDataCache.cleanRange(101L);
    assertNull(contextRepository.findById(101L).get().getRange());
  }

  @Test
  void cleanCategory() {
    userDataCache.setCategory(101L, Category.OOP);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getCategory(), Category.OOP);

    userDataCache.cleanCategory(101L);
    assertNull(contextRepository.findById(101L).get().getCategory());
  }

  @Test
  void cleanLevel() {
    userDataCache.setUserLevel(101L, Level.JUNIOR);
    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(contextRepository.findById(101L).get().getLevel(), Level.JUNIOR);

    userDataCache.cleanLevel(101L);
    assertNull(contextRepository.findById(101L).get().getLevel());
  }

  @Test
  void cleanAll() {
    userDataCache.setUserCurrentBotState(100L, BotState.SHOW_START_MENU);
    userDataCache.setSearchField(101L, "abc");
    userDataCache.setRange(101L, rangePair);
    userDataCache.setCategory(101L, Category.OOP);
    userDataCache.setUserLevel(101L, Level.JUNIOR);

    assertTrue(contextRepository.findById(101L).isPresent());
    assertEquals(userDataCache.getUserCurrentBotState(100L), BotState.SHOW_START_MENU);
    assertEquals(contextRepository.findById(101L).get().getSearchField(), "abc");
    assertEquals(contextRepository.findById(101L).get().getRange(), rangePair);
    assertEquals(contextRepository.findById(101L).get().getCategory(), Category.OOP);
    assertEquals(contextRepository.findById(101L).get().getLevel(), Level.JUNIOR);

    userDataCache.cleanAll(101L);

    assertNull(userDataCache.getUserCurrentBotState(100L));
    assertEquals(contextRepository.findById(101L).get().getSearchField(), "");
    assertNull(contextRepository.findById(101L).get().getRange());
    assertNull(contextRepository.findById(101L).get().getCategory());
    assertNull(contextRepository.findById(101L).get().getLevel());
  }

  @Test
  void getUserContext() {
    assertEquals(userDataCache.getUserContext(101L).getUserId(), 101L);
  }

  @Test
  void setUserContext() {
    UserContext uc = GenericBuilder.of(UserContext::new)
        .with(UserContext::setUserId, 101L)
        .with(UserContext::setCategory, Category.COLLECTIONS)
        .build();

    userDataCache.setUserContext(uc);

    assertEquals(userDataCache.getUserContext(101L).getCategory(), Category.COLLECTIONS);
  }
}