package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

/**
 * Состояние пользователя
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserState {

  /**
   * Id пользователя
   */
  @Id
  long userId;

  /**
   * Состояние
   */
  BotState botState;

  public UserState(long userId) {
    this.userId = userId;
    botState = BotState.SHOW_START_MENU;
  }
}
