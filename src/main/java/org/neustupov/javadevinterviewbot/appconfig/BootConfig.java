package org.neustupov.javadevinterviewbot.appconfig;

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
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Getter
@Setter
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "telegrambot")
public class BootConfig {

  String webHookPath;
  String botUserName;
  String botToken;

  @Bean
  public JavaDevInterviewBot javaDevInterviewBot(TelegramFacade telegramFacade) {
    DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
    JavaDevInterviewBot javaDevInterviewBot = new JavaDevInterviewBot(options, telegramFacade);
    javaDevInterviewBot.setWebHookPath(webHookPath);
    javaDevInterviewBot.setBotUserName(botUserName);
    javaDevInterviewBot.setBotToken(botToken);
    return javaDevInterviewBot;
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }
}
