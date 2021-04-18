package org.neustupov.javadevinterviewbot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Эмодзи
 */
@AllArgsConstructor
@FieldDefaults(makeFinal=true, level = AccessLevel.PRIVATE)
public enum Emojis {

  QUESTION(EmojiParser.parseToUnicode(":question:")),
  EYES(EmojiParser.parseToUnicode(":eyes:")),
  WAVE(EmojiParser.parseToUnicode(":wave:")),
  PREVIOUS(EmojiParser.parseToUnicode(":arrow_left:")),
  NEXT(EmojiParser.parseToUnicode(":arrow_right:")),
  TOP(EmojiParser.parseToUnicode(":arrow_up:")),
  IN_DEV(EmojiParser.parseToUnicode(":hammer_and_wrench:")),
  MAN_STUDENT(EmojiParser.parseToUnicode(":man_student:"));

  /**
   * Название эмодзи
   */
  String emojiName;

  /**
   * Возвращает название эмодзи
   *
   * @return Название эмодзи
   */
  @Override
  public String toString() {
    return emojiName;
  }
}
