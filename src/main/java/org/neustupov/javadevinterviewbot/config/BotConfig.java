package org.neustupov.javadevinterviewbot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.neustupov.javadevinterviewbot.JavaDevInterviewBot;
import org.neustupov.javadevinterviewbot.botapi.TelegramFacade;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Основная конфигурация бота
 */
@Getter
@Setter
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {

  /**
   * Путь к веб хуку - при тестировании используется ngrok.io
   */
  String webHookPath;

  /**
   * Название бота
   */
  String botUserName;

  /**
   * Токен бота
   */
  String botToken;

  /**
   * Бин бота
   *
   * @param telegramFacade Основной фасад бота
   * @return Бин бота
   */
  @Bean
  public JavaDevInterviewBot javaDevInterviewBot(TelegramFacade telegramFacade) {
    JavaDevInterviewBot javaDevInterviewBot = new JavaDevInterviewBot(telegramFacade);
    javaDevInterviewBot.setWebHookPath(webHookPath);
    javaDevInterviewBot.setBotUserName(botUserName);
    javaDevInterviewBot.setBotToken(botToken);
    return javaDevInterviewBot;
  }

  /**
   * Бин ресурсов сообщений
   *
   * @return MessageSource
   */
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
