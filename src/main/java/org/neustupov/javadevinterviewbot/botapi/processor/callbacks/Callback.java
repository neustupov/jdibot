package org.neustupov.javadevinterviewbot.botapi.processor.callbacks;

import org.neustupov.javadevinterviewbot.cache.DataCache;
import org.neustupov.javadevinterviewbot.model.buttons.ButtonCallbacks;
import org.neustupov.javadevinterviewbot.utils.Emojis;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Основной интерфейс для обработки колбеков
 */
public interface Callback {

  /**
   * Сообщение для пунктов меню, которые ещё не реализованы
   */
  String NOT_WORK = Emojis.IN_DEV + " Раздел находится в разработке. " + Emojis.IN_DEV;

  /**
   * Обрабатывает колбек
   *
   * @param callbackQuery Колбек
   * @param callbackData Данные колбека
   * @param dataCache Кэш данных пользователя
   * @param userId userId
   * @param message Сообщение
   * @return Ответ приложения
   */
  BotApiMethod<?> handleCallback(CallbackQuery callbackQuery, ButtonCallbacks callbackData,
      DataCache dataCache, int userId, Message message);

  /**
   * Отправляет сообщение пользователю в виде всплывающего окна
   *
   * @param text Текст сообщения
   * @param alert Необходимость алерта
   * @param callbackQuery Колбек
   * @return AnswerCallbackQuery
   */
  default AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert,
      CallbackQuery callbackQuery) {
    return AnswerCallbackQuery.builder()
        .callbackQueryId(callbackQuery.getId())
        .showAlert(alert)
        .text(text)
        .build();
  }
}
