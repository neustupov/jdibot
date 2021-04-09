package org.neustupov.javadevinterviewbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Конфигурация аутентификации и авторизации - используется in-memory реализация
 */
@Configuration
public class QuestionSecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * Определение пользователей с ролями
   *
   * @param auth AuthenticationManagerBuilder
   * @throws Exception Exception
   */
  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .passwordEncoder(passwordEncoder())
        .withUser("qloader")
        .password(passwordEncoder().encode("erteet7e87ter$hkj"))
        .roles("ADMIN");
  }

  /**
   * Конфигурация доступов - в данном случае ограничиваем доступ к "/api" для обеспечения
   * безопасности работы с базой вопросов
   *
   * @param http HttpSecurity
   * @throws Exception Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/api").hasRole("ADMIN")
        .antMatchers("/").permitAll()
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  /**
   * Бин декодера паролей
   *
   * @return BCryptPasswordEncoder
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
