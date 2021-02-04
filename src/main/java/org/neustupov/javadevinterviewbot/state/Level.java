package org.neustupov.javadevinterviewbot.state;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Level implements State {

  private StateContext context;
  private final Start start;

  @Autowired
  public Level(StateContext context, Start start) {
    this.context = context;
    this.start = start;
  }

  @Override
  public void next() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void previous() {
    context.setState(start);
  }
}
