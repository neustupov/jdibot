package org.neustupov.javadevinterviewbot.repo;

import org.neustupov.javadevinterviewbot.model.UserState;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatesRepository extends MongoRepository<UserState, Long> {

  UserState getByChatId(long chatId);
}
