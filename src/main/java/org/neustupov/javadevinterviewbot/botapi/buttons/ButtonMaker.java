package org.neustupov.javadevinterviewbot.botapi.buttons;

import static java.util.stream.Collectors.toList;
import static org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks.*;
import static org.neustupov.javadevinterviewbot.model.buttons.ButtonNames.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.neustupov.javadevinterviewbot.model.BotState;
import org.neustupov.javadevinterviewbot.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

/**
 * Класс формирует кнопки меню
 */
@Component
public class ButtonMaker {

  /**
   * Формирует блок клавиатуры сообщения из мапы названий\кобеков кнопок и, в зависимости от
   * состояния, добавляет кнопки возврата к предыдущему состоянию
   *
   * @param buttonMap Мапа названий\кобеков кнопок
   * @param botState Текущее состояние
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getInlineMessageButtons(Map<String, String> buttonMap,
      BotState botState) {
    List<InlineKeyboardButton> menuButtons = buttonMap.keySet()
            .stream()
            .map(key -> InlineKeyboardButton.builder().text(key).callbackData(buttonMap.get(key)).build())
            .collect(toList());
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(menuButtons);
    if (botState.equals(BotState.SHOW_CATEGORY_MENU)) {
      rows.add(getBackToLevel());
    } else if (botState.equals(BotState.SHOW_LEVEL_MENU)) {
      rows.add(getBackToStart());
    }
    return InlineKeyboardMarkup.builder().keyboard(rows).build();
  }

  /**
   * Формирует блок клавиатуры для возврата в предыдущее состояние
   *
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getBackButton() {
    return InlineKeyboardMarkup.builder()
        .keyboard(Collections.singletonList(getSimpleBackButton()))
        .build();
  }

  /**
   * Формирует блок клавиатуры для возврата в главное меню бота
   *
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getBackToStartMenuButton() {
    return InlineKeyboardMarkup.builder()
        .keyboard(Collections.singletonList(getBackToStart()))
        .build();
  }

  /**
   * Формирует блок клавиатуры пагинации с кнопками возврата
   *
   * @return Блок клавиатуры
   */
  public InlineKeyboardMarkup getPaginationButton(boolean previous, boolean next, BotState state) {
    List<InlineKeyboardButton> paginationButton = new ArrayList<>();
    if (previous) {
      InlineKeyboardButton buttonPrevious = InlineKeyboardButton.builder()
          .text(Emojis.PREVIOUS + " " + PREVIOUS)
          .callbackData(PREVIOUS_BUTTON.toString())
          .build();
      paginationButton.add(buttonPrevious);
    }
    if (next) {
      InlineKeyboardButton buttonNext = InlineKeyboardButton.builder()
          .text(NEXT + " " + Emojis.NEXT)
          .callbackData(NEXT_BUTTON.toString())
          .build();
      paginationButton.add(buttonNext);
    }
    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
    rows.add(paginationButton);
    switch (state) {
      case FILLING_SEARCH:
        rows.add(getBackToStart());
        break;
      case SHOW_CATEGORY:
        rows.add(getBackToCategory());
        break;
    }
    return InlineKeyboardMarkup.builder().keyboard(rows).build();
  }

  /**
   * Формирует кнопки возврата к предыдущему состоянию
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getSimpleBackButton() {
    return getInlineKeyboardButtons(Emojis.TOP + " " + BACK, BACK_BUTTON.toString());
  }

  /**
   * Формирует кнопки возврата к выбору уровня
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getBackToLevel() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_LEVEL + "   " + Emojis.TOP,
        BACK_TO_LEVEL_BUTTON.toString());
  }

  /**
   * Формирует кнопки возврата к главному меню
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getBackToStart() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_START_MENU + "   " + Emojis.TOP,
        BACK_TO_START_MENU_BUTTON.toString());
  }

  /**
   * Формирует кнопки возврата к выбору категорий
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getBackToCategory() {
    return getInlineKeyboardButtons(Emojis.TOP + "   " + BACK_TO_CATEGORY + "   " + Emojis.TOP,
        BACK_TO_CATEGORY_BUTTON.toString());
  }

  /**
   * Формирует список кнопок
   *
   * @return Список кнопок
   */
  private List<InlineKeyboardButton> getInlineKeyboardButtons(String button,
      String callback) {
    InlineKeyboardButton backToStart = InlineKeyboardButton.builder()
        .text(button)
        .callbackData(callback)
        .build();
    return Collections.singletonList(backToStart);
  }
}
