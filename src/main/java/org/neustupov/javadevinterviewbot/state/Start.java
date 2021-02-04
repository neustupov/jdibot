package org.neustupov.javadevinterviewbot.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Start implements State {

  private StateContext context;
  private Level level;

  public Start(StateContext context, Level level) {
    this.context = context;
    this.level = level;
  }

  @Override
  public void next() {
    context.setState(level);
  }

  @Override
  public void previous() {
    throw new UnsupportedOperationException();
  }
}
