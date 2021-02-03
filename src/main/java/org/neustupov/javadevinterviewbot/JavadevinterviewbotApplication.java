package org.neustupov.javadevinterviewbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class JavadevinterviewbotApplication {

  public static void main(String[] args) {
    ApiContextInitializer.init();
    SpringApplication.run(JavadevinterviewbotApplication.class, args);
  }

}
