package org.neustupov.javadevinterviewbot.model;

/**
 * Состояния бота
 */
public enum  BotState {

  SHOW_START_MENU,
  SHOW_SEARCH,
  SHOW_LEVEL_MENU,
  SHOW_CATEGORY_MENU,
  SHOW_SPRING_CATEGORY_MENU,
  SHOW_CATEGORY,
  SHOW_QUESTION,

  CATEGORY_OR_SEARCH_RESULT,

  FILLING_SEARCH,

  PAGINATION_PAGE
}
