package org.neustupov.javadevinterviewbot.config;

import lombok.Getter;
import lombok.Setter;
import org.neustupov.javadevinterviewbot.JavaDevInterviewBot;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BootConfig {

  private String webHookPath;
  private String botUserName;
  private String botToken;

  @Bean
  public JavaDevInterviewBot JavaDevInterviewBot() {
    DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
    JavaDevInterviewBot javaDevInterviewBot = new JavaDevInterviewBot(options);
    javaDevInterviewBot.setWebHookPath(webHookPath);
    javaDevInterviewBot.setBotUserName(botUserName);
    javaDevInterviewBot.setBotToken(botToken);
    return javaDevInterviewBot;
  }
}
