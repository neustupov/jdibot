package org.neustupov.javadevinterviewbot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {

  QUESTION(EmojiParser.parseToUnicode(":question:")),
  EYES(EmojiParser.parseToUnicode(":eyes:")),
  WAVE(EmojiParser.parseToUnicode(":wave:")),
  PREVIOUS(EmojiParser.parseToUnicode(":arrow_left:")),
  NEXT(EmojiParser.parseToUnicode(":arrow_right:")),
  TOP(EmojiParser.parseToUnicode(":arrow_up:")),
  IN_DEV(EmojiParser.parseToUnicode(":hammer_and_wrench:")),
  MAN_STUDENT(EmojiParser.parseToUnicode(":man_student:"));

  private String emojiName;

  @Override
  public String toString() {
    return emojiName;
  }
}
