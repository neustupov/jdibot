package org.neustupov.javadevinterviewbot.service;

import java.util.Locale;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Сервис локализации сообщений
 */
@Service
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public class LocaleMessageService {

  /**
   * Локаль
   */
  Locale locale;

  /**
   * Сообщение
   */
  MessageSource messageSource;

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
