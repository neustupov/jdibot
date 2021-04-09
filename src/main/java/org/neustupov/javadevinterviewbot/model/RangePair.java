package org.neustupov.javadevinterviewbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Бин диапазона для пагинации
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RangePair {

  /**
   * От
   */
  Integer from;

  /**
   * До
   */
  Integer to;
}
