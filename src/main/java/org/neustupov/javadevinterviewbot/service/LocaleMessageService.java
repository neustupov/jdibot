package org.neustupov.javadevinterviewbot.service;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Сервис локализации сообщений
 */
@Service
public class LocaleMessageService {

  /**
   * Локаль
   */
  private final Locale locale;

  /**
   * Сообщение
   */
  private final MessageSource messageSource;

  public LocaleMessageService(@Value("ru-RU") String localeTag, MessageSource messageSource) {
    this.locale = Locale.forLanguageTag(localeTag);
    this.messageSource = messageSource;
  }

  /**
   * Возвращает локализованное сообщение
   *
   * @param message Сообщение
   * @return Локализованное сообщение
   */
  String getMessage(String message) {
    return messageSource.getMessage(message, null, locale);
  }

  /**
   * Возвращает локализованное сообщение
   *
   * @param message Сообщение
   * @param args Дополнительные аргументы
   * @return Локализованное сообщение
   */
  String getMessage(String message, Object... args) {
    return messageSource.getMessage(message, args, locale);
  }
}
