package org.neustupov.javadevinterviewbot.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для пагинации и дополнительных свойств приложения
 */
@Getter
@Setter
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "app")
public class AppConfig {

  /**
   * Количество вопросов в сообщении
   */
  Integer pagination;

  /**
   * Путь к изображению для стартового сообщения
   */
  String startImage;
}
