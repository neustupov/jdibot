package org.neustupov.javadevinterviewbot.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.neustupov.javadevinterviewbot.repo.StatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class StateContext {

  @Getter
  @Setter
  private State state;

  private StatesRepository statesRepository;

  @Autowired
  public StateContext(StatesRepository statesRepository) {
    this.statesRepository = statesRepository;
  }

  public State getUserState(long chatId){
    return statesRepository.getByChatId(chatId).getState();
  }
}
