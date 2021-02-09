package org.neustupov.javadevinterviewbot.service;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class LocaleMessageService {

  private final Locale locale;
  private final MessageSource messageSource;

  public LocaleMessageService(@Value("ru-RU") String localeTag, MessageSource messageSource) {
    this.locale = Locale.forLanguageTag(localeTag);
    this.messageSource = messageSource;
  }

  String getMessage(String message) {
    return messageSource.getMessage(message, null, locale);
  }

  String getMessage(String message, Object... args) {
    return messageSource.getMessage(message, args, locale);
  }
}
