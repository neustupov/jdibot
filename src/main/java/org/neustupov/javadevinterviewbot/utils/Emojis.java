package org.neustupov.javadevinterviewbot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {

  QUESTION(EmojiParser.parseToUnicode(":question:")),
  EYES(EmojiParser.parseToUnicode(":eyes:")),
  WAVE(EmojiParser.parseToUnicode(":wave:")),
  BACK(EmojiParser.parseToUnicode(":back:")),
  TOP(EmojiParser.parseToUnicode(":top:")),
  MAN_STUDENT(EmojiParser.parseToUnicode(":man_student:"));

  private String emojiName;

  @Override
  public String toString() {
    return emojiName;
  }
}
